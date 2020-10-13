resource "aws_s3_bucket" "netlit_books" {
  bucket = "book.${var.domain_name}"
  acl = "public-read"
  policy = "${file("${path.module}/s3-bucket-policy.json")}"

  website {
    index_document = "index.html"
  }

  cors_rule {
    allowed_origins = [
      "*"
    ]
    allowed_methods = [
      "GET"
    ]
    allowed_headers = [
      "*"
    ]
    max_age_seconds = 1800
  }

  depends_on = [
    "aws_cloudfront_origin_access_identity.origin_access_identity",
    "data.template_file.aws_s3_bucket_policy"
  ]

  tags {
    Environment = "prd"
  }
}

resource "aws_cloudfront_origin_access_identity" "origin_access_identity" {
  comment = "Identity with grant access to the private bucket"
}

resource "local_file" "aws_s3_bucket_policy" {
  content = "${data.template_file.aws_s3_bucket_policy.rendered}"
  filename = "${path.root}/s3-bucket-policy.json"
}

data "template_file" "aws_s3_bucket_policy" {
  template = "${file("${path.module}/s3-bucket-policy.json.tpl")}"

  vars {
    s3_canonical_user = "${aws_cloudfront_origin_access_identity.origin_access_identity.iam_arn}"
    bucket_name = "book.${var.domain_name}"
  }
}