module "authorization_server" {
  source = "../modules/ecs-service"

  application_name = "authorization-server"
  repository_url = "${data.aws_ecr_repository.authorization_server.repository_url}"
  container_version = "1.0.1"
  task_role = "${aws_iam_role.authorization_server_role.arn}"
  cluster = "${aws_ecs_cluster.netlit.arn}"
  security_groups = "${data.aws_security_groups.security_groups.ids}"
  vpc = "${data.aws_vpc.main.id}"
  health_check_path = "/v1/actuator/health"
  load_balancer_listener = "${data.aws_lb_listener.listener.arn}"
  dns_namespace_id = "${var.dns_namespace_id}"
  exposed_resources = [
    "/v1/oauth/*"
  ]
  environment_variables = <<EOF
[
  {
    "name": "SPRING_PROFILES_ACTIVE",
    "value": "prod"
  },
  {
    "name": "AWS_SQS_QUEUE",
    "value": "${data.aws_sqs_queue.authorization_server_queue.url}"
  },
  {
    "name": "AWS_ACCESS_KEY_ID",
    "value": "AKIAW6XF2QZI56OHM2MH"
  },
  {
    "name": "AWS_SECRET_ACCESS_KEY",
    "value": "WGRTL8lciINsDVNMVqT/6lkxrMCpPydOODrw2YzS"
  }
]
EOF
}

resource "aws_iam_role" "authorization_server_role" {
  name = "authorization-server-${var.profile_environment}-role"

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

resource "aws_iam_role_policy" "authorization_server_dynamodb_policy" {
  name = "authorization-server-dynamodb-${var.profile_environment}-policy"
  role = "${aws_iam_role.authorization_server_role.id}"

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
        "${aws_dynamodb_table.user_table.arn}",
        "${aws_dynamodb_table.user_table.arn}/*"
      ]
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "authorization_server_sqs_policy" {
  name = "authorization-server-sqs-${var.profile_environment}-policy"
  role = "${aws_iam_role.authorization_server_role.id}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AmazonSQSReadAccess",
      "Effect": "Allow",
      "Action": [
        "sqs:DeleteMessage",
        "sqs:ReceiveMessage"
      ],
      "Resource": "${data.aws_sqs_queue.authorization_server_queue.arn}"
    }
  ]
}
EOF
}

resource "aws_dynamodb_table" "user_table" {
  name = "prod.User"
  hash_key = "Email"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Email"
    type = "S"
  }
}
