variable "aws_region" {
  description = "The AWS region things are created in"
  default = "us-east-2"
}

variable "profile_environment" {
  description = "The AWS environemnt in witch the system is running"
  default = "stg"
}

variable "az_count" {
  description = "Number of AZs to cover in a given region"
  default = "2"
}

variable "app_port" {
  description = "Port exposed by the docker image to redirect traffic to"
  default = 8080
}

variable "public_port" {
  description = "Port exposed by the load balancer to the world"
  default = 80
}

variable "local_domain_name" {
  default = "netlit.local.stg"
}