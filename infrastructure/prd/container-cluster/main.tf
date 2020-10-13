terraform {
  required_version = ">= 0.11.13"
}

provider "aws" {
  region = "${var.aws_region}"
  profile = "${var.profile_environment}"
}

resource "aws_ecs_cluster" "netlit" {
  name = "netlit-cluster"
}

///////////////////////////////////////////

data "aws_lb_listener" "listener" {
  arn = "${var.lb_listener_arn}"
}

data "aws_vpc" "main" {
  id = "${var.vpc_id}"
}

data "aws_security_groups" "security_groups" {
  filter {
    name = "group-name"
    values = [
      "*ecs-tasks*"
    ]
  }
}

#variable "dns_namespace_id" {
#  default = "ns-6slscwx3d5mzixts"
#}

data "aws_ecr_repository" "accounts" {
  name = "accounts"
}

data "aws_sns_topic" "credentials_created_topic" {
  name = "credentials-created-topic"
}

data "aws_sns_topic" "credentials_updated_topic" {
  name = "credentials-updated-topic"
}

data "aws_sns_topic" "credit_card_registration_topic" {
  name = "credit-card-registration-topic"
}

data "aws_sqs_queue" "authorization_server_queue" {
  name = "authorization-server-queue"
}

data "aws_sqs_queue" "payments_queue" {
  name = "payments-queue"
}

data "aws_ecr_repository" "authorization_server" {
  name = "authorization-server"
}

data "aws_ecr_repository" "payments" {
  name = "payments"
}

data "aws_ecr_repository" "library" {
  name = "library"
}

data "aws_ecr_repository" "customer_service" {
  name = "customer-service"
}

data "aws_sns_topic" "sendmail_sns" {
  name = "sendmail-sns"
}
