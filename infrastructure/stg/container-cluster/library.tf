module "library" {
  source = "../modules/ecs-service"

  application_name = "library"
  repository_url = "${data.aws_ecr_repository.library.repository_url}"
  container_version = "1.0.2-SNAPSHOT"
  task_role = "${aws_iam_role.library_role.arn}"
  cluster = "${aws_ecs_cluster.netlit.arn}"
  security_groups = "${data.aws_security_groups.security_groups.ids}"
  vpc = "${data.aws_vpc.main.id}"
  health_check_path = "/v1/actuator/health"
  load_balancer_listener = "${data.aws_lb_listener.listener.arn}"
  dns_namespace_id = "${var.dns_namespace_id}"
  exposed_resources = [
    "/v1/bookmarks*",
    "/v1/books*",
    "/v1/highlights*",
    "/v1/latest-readings*",
    "/v1/pages/*",
    "/v1/review*",
    "/v1/filter*"
  ]
  environment_variables = <<EOF
[
  {
    "name": "SPRING_PROFILES_ACTIVE",
    "value": "dev"
  },
  {
    "name": "AUTHORIZATION_SERVER_URI",
    "value": "http://${module.authorization_server.discovery_dns}:8080/v1/oauth/user"
  },
  {
    "name": "BOOK_ADDRESS",
    "value": "http://netlit-books.s3-website.us-east-2.amazonaws.com"
  },
  {
    "name": "BOOK_ADDRESS_CONTENT",
    "value": "http://cloud-reader.s3-website-us-east-1.amazonaws.com/?epub=epub_content/"
  },
  {
    "name": "DATABASE_ENDPOINT_NAME",
    "value": "${var.library_db_endpoint}"
  },
  {
    "name": "DATABASE_USERNAME",
    "value": "${var.library_db_username}"
  },
  {
    "name": "DATABASE_PASSWORD",
    "value": "${var.library_db_password}"
  },
  {
  	"name": "AWS_BUCKET",
  	"value": "cloud-reader/epub_content"
  }
]
EOF
}

resource "aws_iam_role" "library_role" {
  name = "library-${var.profile_environment}-role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "EcsTaskExecutionRole",
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF

  tags {
    Environment = "${var.profile_environment}"
  }
}

resource "aws_iam_role_policy" "library_dynamodb_policy" {
  name = "library-dynamodb-${var.profile_environment}-policy"
  role = "${aws_iam_role.library_role.id}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AmazonDynamoDBReadWrite",
      "Effect": "Allow",
      "Action": [
        "dynamodb:BatchGetItem",
        "dynamodb:BatchWriteItem",
        "dynamodb:PutItem",
        "dynamodb:DeleteItem",
        "dynamodb:GetItem",
        "dynamodb:Query",
        "dynamodb:UpdateItem"
      ],
      "Resource": [
        "${aws_dynamodb_table.read_page_table.arn}",
        "${aws_dynamodb_table.read_page_table.arn}/*",
        "${aws_dynamodb_table.read_book_table.arn}",
        "${aws_dynamodb_table.read_book_table.arn}/*",
        "${aws_dynamodb_table.bookmark_table.arn}",
        "${aws_dynamodb_table.bookmark_table.arn}/*",
        "${aws_dynamodb_table.review_table.arn}",
        "${aws_dynamodb_table.review_table.arn}/*"
      ]
    }
  ]
}
EOF
}

resource "aws_dynamodb_table" "read_page_table" {
  name = "dev.ReadPage"
  hash_key = "Id"
  range_key = "ReadAt"
  write_capacity = 1
  read_capacity = 1

  attribute {
    name = "Id"
    type = "S"
  }
  attribute {
    name = "ReadAt"
    type = "S"
  }
}

resource "aws_dynamodb_table" "read_book_table" {
  name = "dev.ReadBook"
  hash_key = "Id"
  write_capacity = 1
  read_capacity = 1

  attribute {
    name = "AccountId"
    type = "S"
  }
  attribute {
    name = "Id"
    type = "S"
  }
  attribute {
    name = "ReadAt"
    type = "S"
  }
  global_secondary_index {
    hash_key = "AccountId"
    range_key = "ReadAt"
    name = "AccountIndex"
    projection_type = "ALL"

    write_capacity = 1
    read_capacity = 1
  }
}

resource "aws_dynamodb_table" "bookmark_table" {
  name = "dev.Bookmark"
  hash_key = "AccountId"
  range_key = "BookId"
  write_capacity = 1
  read_capacity = 1

  attribute {
    name = "AccountId"
    type = "S"
  }
  attribute {
    name = "BookId"
    type = "S"
  }

}

resource "aws_dynamodb_table" "review_table" {
  name = "dev.Review"
  hash_key = "Email"
  range_key = "BookId"
  write_capacity = 1
  read_capacity = 1

  attribute {
    name = "Email"
    type = "S"
  }
  attribute {
    name = "BookId"
    type = "S"
  }

}