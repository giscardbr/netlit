terraform {
  required_version = ">= 0.11.13"
}

provider "aws" {
  region = "us-east-1"
  profile = "prd"
}

resource "aws_s3_bucket" "netlit_root" {
  bucket = "${var.domain_name}"
  acl = "public-read"
  policy = "${file("${path.module}/s3-bucket-policy.json")}"

  website {
    index_document = "index.html"
    error_document = "index.html"
  }

  depends_on = [
    "data.template_file.aws_s3_bucket_policy"
  ]

  tags {
    Environment = "prd"
  }
}

resource "local_file" "aws_s3_bucket_policy" {
  content = "${data.template_file.aws_s3_bucket_policy.rendered}"
  filename = "${path.root}/s3-bucket-policy.json"
}

data "template_file" "aws_s3_bucket_policy" {
  template = "${file("${path.module}/s3-bucket-policy.json.tpl")}"

  vars {
    bucket_name = "${var.domain_name}"
  }
}