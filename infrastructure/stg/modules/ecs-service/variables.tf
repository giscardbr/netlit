variable "application_name" {
  description = "The name of the related application"
}

variable "container_version" {
  description = "Docker image version to run in the ECS cluster"
}

variable "task_role" {
  description = "The ARN of Amazon ECS Task Execution IAM Role"
}

variable "cluster" {
  description = "The ARN of Amazon ECS cluster"
}

variable "security_groups" {
  type = "list"
  description = "The security groups associated with the service"
}

variable "vpc" {
  description = "The subnets associated with the service"
}

variable "load_balancer_listener" {
  description = "The ARN of the listener to which to attach the Target Group"
}

variable "cpu" {
  description = "Fargate instance CPU units to provision (1 vCPU = 1024 CPU units)"
  default = "512"
}

variable "memory" {
  description = "Fargate instance memory to provision (in MiB)"
  default = "1024"
}

variable "aws_region" {
  description = "The AWS region things are created in"
  default = "us-east-2"
}

variable "container_port" {
  description = "Port exposed by the docker image to redirect traffic to"
  default = 8080
}

variable "replicas" {
  description = "Number of docker containers to run"
  default = 1
}

variable "health_check_path" {
  description = "The destination for the health check request"
}

variable "exposed_resources" {
  description = ""
  type = "list"
}

variable "environment_variables" {
  description = "The dynamic-named values that can affect the way running containers will behave"
  default = <<EOF
[

]
EOF
}

variable "dns_namespace_id" {
}

variable "repository_url" {}