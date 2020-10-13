module "customer_service" {
  source = "../modules/ecs-service"

  application_name = "customer-service"
  repository_url = "${data.aws_ecr_repository.customer_service.repository_url}"
  container_version = "1.0.1"
  task_role = "${aws_iam_role.customer_service_role.arn}"
  cluster = "${aws_ecs_cluster.netlit.arn}"
  security_groups = "${data.aws_security_groups.security_groups.ids}"
  vpc = "${data.aws_vpc.main.id}"
  health_check_path = "/v1/actuator/health"
  load_balancer_listener = "${data.aws_lb_listener.listener.arn}"
  dns_namespace_id = "${var.dns_namespace_id}"
  exposed_resources = [
    "/v1/contact-us*",
    "/v1/email-sales*",
    "/v1/school-city*"
  ]
  environment_variables = <<EOF
[
  {
    "name": "EMAIL",
    "value": "no-reply@editoradobrasil.com.br"
  },
  {
    "name": "PASSWORD",
    "value": "MkTEb$a35"
  },
  {
    "name": "EMAIL_TO",
    "value": "atendimento@editoradobrasil.com.br"
  },
  {
    "name": "EMAIL_SALES1_TO",
    "value": "caren.francine@editoradobrasil.com.br"
  },
  {
    "name": "EMAIL_SALES2_TO",
    "value": "helena.leitao@editoradobrasil.com.br"
  }
]
EOF
}

resource "aws_iam_role" "customer_service_role" {
  name = "customer-service-role"

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
