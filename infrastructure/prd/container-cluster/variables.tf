variable "aws_region" {
  description = "The AWS region things are created in"
  default = "us-east-1"
}

variable "profile_environment" {
  description = "The AWS environemnt in witch the system is running"
  default = "prd"
}

variable "local_domain_name" {
  default = "netlit.local.prd"
}

variable "library_db_endpoint" {
  default = "library-1.cluster-cglh2op0opbl.us-east-1.rds.amazonaws.com"
}

variable "library_db_username" {
  default = "root"
}

variable "library_db_password" {
  default = "n371!7_1!8r4ry_d4748453"
}

variable "payments_db_endpoint" {
  default = "payments.cglh2op0opbl.us-east-1.rds.amazonaws.com"
}

variable "payments_db_username" {
  default = "KqwU76juxEcXtCdf"
}

variable "payments_db_password" {
  default = "AJT8EPYcNGxMqpKm"
}

variable "lb_listener_arn" {
  default = "arn:aws:elasticloadbalancing:us-east-1:478297097809:listener/app/netlit-load-balancer/045842413b4eb2c5/6b7c2167a381520f"
}

variable "vpc_id" {
  default = "vpc-0eba7e3e845311627"
}

variable "dns_namespace_id" {
  default = "ns-6slscwx3d5mzixts"
}
