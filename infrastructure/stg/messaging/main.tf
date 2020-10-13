terraform {
  required_version = ">= 0.11.13"
}

provider "aws" {
  region = "${var.aws_region}"
  profile = "${var.profile_environment}"
}