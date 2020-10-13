variable "aws_region" {
  description = "The AWS region things are created in"
  default = "us-east-2"
}

variable "profile_environment" {
  description = "The AWS environemnt in witch the system is running"
  default = "stg"
}

variable "local_domain_name" {
  default = "netlit.local.stg"
}

variable "library_db_endpoint" {
  default = "library.cwqhhhkhaxkf.us-east-2.rds.amazonaws.com"
}

variable "library_db_username" {
  default = "ARd4FmUTZbt3CXaJ"
}

variable "library_db_password" {
  default = "w8PKJzE2axANmk4s"
}

variable "payments_db_endpoint" {
  default = "payments.cwqhhhkhaxkf.us-east-2.rds.amazonaws.com"
}

variable "payments_db_username" {
  default = "KqwU76juxEcXtCdf"
}

variable "payments_db_password" {
  default = "AJT8EPYcNGxMqpKm"
}

variable "lb_listener_arn" {
  default = "arn:aws:elasticloadbalancing:us-east-2:478297097809:listener/app/netlit-load-balancer/e46ba5355555117e/eff5f74fa2f7ee8a"
}

variable "vpc_id" {
  default = "vpc-092993e2dee571ef9"
}

variable "dns_namespace_id" {
  default = "ns-wgky7wzb75zfrsm4"
}