module "accounts" {
  source = "../modules/ecs-service"

  application_name = "accounts"
  repository_url = "${data.aws_ecr_repository.accounts.repository_url}"
  container_version = "1.0.5-SNAPSHOT"
  task_role = "${aws_iam_role.accounts_role.arn}"
  cluster = "${aws_ecs_cluster.netlit.arn}"
  security_groups = "${data.aws_security_groups.security_groups.ids}"
  vpc = "${data.aws_vpc.main.id}"
  health_check_path = "/v1/actuator/health"
  load_balancer_listener = "${data.aws_lb_listener.listener.arn}"
  dns_namespace_id = "${var.dns_namespace_id}"
  exposed_resources = [
    "/v1/accounts*",
    "/v1/*-accounts*",
    "/v1/passwords*",
    "/v1/processes/*",
    "/v1/v2/api-docs",
    "/v1/csrf",
    "/v1/configuration/ui",
    "/v1/swagger-resources/**",
    "/v1/configuration/security/**",
    "/v1/swagger-ui.html",
    "/v1/webjars/**",
    "/v1/registration*",
    "/v1/orders*",
    "/v1/uploads*",
    "/v1/tests",
    "/v1/school-grade-rooms*"
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
    "name": "AWS_SNS_TOPIC",
    "value": "${data.aws_sns_topic.credentials_created_topic.arn}"
  },
  {
    "name": "SNS_CREDENTIALS_UPDATED_TOPIC",
    "value": "${data.aws_sns_topic.credentials_updated_topic.arn}"
  },
  {
    "name": "SPRING_MAIL_USERNAME",
    "value": "no-reply@editoradobrasil.com.br"
  },
  {
    "name": "SPRING_MAIL_PASSWORD",
    "value": "MkTEb$a35"
  },
  {
    "name": "SNS_CREDIT_CARD_REGISTRATION_TOPIC",
    "value": "${data.aws_sns_topic.credit_card_registration_topic.arn}"
  },
  {
    "name": "SPRING_DATASOURCE_URL",
    "value": "jdbc:mysql://payments.cwqhhhkhaxkf.us-east-2.rds.amazonaws.com:3306/payments"
  },
  {
    "name": "SPRING_DATASOURCE_USERNAME",
    "value": "KqwU76juxEcXtCdf"
  },
  {
    "name": "SPRING_DATASOURCE_PASSWORD",
    "value": "AJT8EPYcNGxMqpKm"
  },
  {
    "name": "NETLIT_PROTHEUS_URL",
    "value": "http://200.201.128.38:18238/netlit/PEDIDOSNETLIT.apw?WSDL"
  },
  {
    "name": "AWS_SQS_QUEUE_SENDMAIL",
    "value": "https://sqs.us-east-2.amazonaws.com/478297097809/sendmail"
  },
  {
    "name": "AWS_SNS_TOPIC_SENDMAIL",
    "value": "${data.aws_sns_topic.sendmail_sns.arn}"
  }
]
EOF
}

resource "aws_iam_role" "accounts_role" {
  name = "accounts-${var.profile_environment}-role"

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

resource "aws_iam_role_policy_attachment" "policy-attach" {
  role = "${aws_iam_role.accounts_role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy" "accounts_dynamodb_policy" {
  name = "accounts-dynamodb-${var.profile_environment}-policy"
  role = "${aws_iam_role.accounts_role.id}"

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
        "dynamodb:Scan",
        "dynamodb:UpdateItem"
      ],
      "Resource": [
        "${aws_dynamodb_table.account_table.arn}",
        "${aws_dynamodb_table.account_table.arn}/*",
        "${aws_dynamodb_table.credentials_table.arn}",
        "${aws_dynamodb_table.credentials_table.arn}/*",
        "${aws_dynamodb_table.sub_account_table.arn}",
        "${aws_dynamodb_table.sub_account_table.arn}/*",
        "${aws_dynamodb_table.address_table.arn}",
        "${aws_dynamodb_table.address_table.arn}/*",
        "${aws_dynamodb_table.user_table.arn}",
        "${aws_dynamodb_table.user_table.arn}/*",
        "${aws_dynamodb_table.verification_token_table.arn}",
        "${aws_dynamodb_table.verification_token_table.arn}/*", 
        "${aws_dynamodb_table.school_grade_room_table.arn}",
        "${aws_dynamodb_table.school_grade_room_table.arn}/*",
        "${aws_dynamodb_table.school_room_student_table.arn}",
        "${aws_dynamodb_table.school_room_student_table.arn}/*"
      ]
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "accounts_sns_policy" {
  name = "accounts-sns-${var.profile_environment}-policy"
  role = "${aws_iam_role.accounts_role.id}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AmazonSNSWriteAccess",
      "Effect": "Allow",
      "Action": "sns:Publish",
      "Resource": [
        "${data.aws_sns_topic.credentials_created_topic.arn}",
        "${data.aws_sns_topic.credentials_updated_topic.arn}",
        "${data.aws_sns_topic.credit_card_registration_topic.arn}",
        "${data.aws_sns_topic.sendmail_sns.arn}"
      ]
    }
  ]
}
EOF
}

resource "aws_dynamodb_table" "account_table" {
  name = "dev.Account"
  hash_key = "Id"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Id"
    type = "S"
  }
}

resource "aws_dynamodb_table" "credentials_table" {
  name = "dev.Credentials"
  hash_key = "Email"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Email"
    type = "S"
  }
  attribute {
    name = "AccountId"
    type = "S"
  }
  global_secondary_index {
    hash_key = "AccountId"
    name = "AccountIndex"
    projection_type = "ALL"

    write_capacity = 1
    read_capacity = 1
  }
}

resource "aws_dynamodb_table" "sub_account_table" {
  name = "dev.SubAccount"
  hash_key = "Id"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Id"
    type = "S"
  }
}

resource "aws_dynamodb_table" "address_table" {
  name = "dev.Address"
  hash_key = "Id"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Id"
    type = "S"
  }
}

resource "aws_dynamodb_table" "verification_token_table" {
  name = "dev.VerificationToken"
  hash_key = "Id"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Id"
    type = "S"
  }
}

resource "aws_dynamodb_table" "school_grade_room_table" {
  name = "dev.SchoolGradeRoom"
  hash_key = "Id"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Id"
    type = "S"
  }
}

resource "aws_dynamodb_table" "school_room_student_table" {
  name = "dev.SchoolRoomStudent"
  hash_key = "Id"
  write_capacity = 1
  read_capacity = 1
  attribute {
    name = "Id"
    type = "S"
  }
}
