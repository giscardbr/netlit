data "aws_acm_certificate" "netlit_com_br_certificate" {
  domain = "*.${var.domain_name}"
  statuses = [
    "ISSUED"
  ]
}