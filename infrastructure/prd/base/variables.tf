variable "aws_region" {
  description = "The AWS region things are created in"
  default = "us-east-1"
}

variable "profile_environment" {
  description = "The AWS environemnt in witch the system is running"
  default = "development"
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
  default = 443
}

variable "domain_name" {
  default = "netlit.com.br"
}