module "payments" {
  source = "../modules/ecs-service"

  application_name = "payments"
  repository_url = "${data.aws_ecr_repository.payments.repository_url}"
  container_version = "1.0.0"
  task_role = "${aws_iam_role.payments_role.arn}"
  cluster = "${aws_ecs_cluster.netlit.arn}"
  security_groups = "${data.aws_security_groups.security_groups.ids}"
  vpc = "${data.aws_vpc.main.id}"
  health_check_path = "/v1/actuator/health"
  load_balancer_listener = "${data.aws_lb_listener.listener.arn}"
  dns_namespace_id = "${var.dns_namespace_id}"
  exposed_resources = [
    "/v1/credit-cards*",
    "/v1/payments*",
    "/v1/subscription-plans*"
  ]
  environment_variables = <<EOF
[
  {
    "name": "SPRING_PROFILES_ACTIVE",
    "value": "prod"
  },
  {
    "name": "ADYEN_API_ADDRESS",
    "value": "https://pal-test.adyen.com/pal/servlet"
  },
  {
    "name": "ADYEN_CHECKOUT_API_ADDRESS",
    "value": "https://checkout-test.adyen.com"
  },
  {
    "name": "ADYEN_API_KEY",
    "value": "AQEqhmfuXNWTK0Qc+iSVlm0sqPaabIRvH5JZUWmghokj2pHQnP+ZQEXkXy7NEMFdWw2+5HzctViMSCJMYAc=-vOQXJCIdE95kITymUHHQFH3Xee1I5IfoiHHIivUQxqU=-s9uNXNh7n8MhDctB"
  },
  {
    "name": "ADYEN_MERCHANT_ACCOUNT",
    "value": "EditoraDoBrasilNetLit"
  },
  {
    "name": "AUTHORIZATION_SERVER_URI",
    "value": "http://${module.authorization_server.discovery_dns}:8080/v1/oauth/user"
  },
  {
    "name": "AWS_SQS_QUEUE",
    "value": "${data.aws_sqs_queue.payments_queue.url}"
  },
  {
    "name": "DATABASE_ENDPOINT_NAME",
    "value": "jdbc:mysql://${var.library_db_endpoint}/payments"
  },
  {
    "name": "DATABASE_USERNAME",
    "value": "${var.library_db_username}"
  },
  {
    "name": "DATABASE_PASSWORD",
    "value": "${var.library_db_password}"
  }
]
EOF
}

resource "aws_iam_role" "payments_role" {
  name = "payments-${var.profile_environment}-role"

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
}

resource "aws_iam_role_policy" "payments_sqs_policy" {
  name = "payments-sqs-${var.profile_environment}-policy"
  role = "${aws_iam_role.payments_role.id}"

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
      "Resource": "${data.aws_sqs_queue.payments_queue.arn}"
    }
  ]
}
EOF
}
