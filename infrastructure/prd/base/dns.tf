resource "aws_route53_zone" "netlit_com_br" {
  name = "${var.domain_name}"
  comment = ""

  tags {
    Environment = "prd"
  }
}

resource "aws_route53_record" "api" {
  zone_id = "${aws_route53_zone.netlit_com_br.zone_id}"
  name = "api.${var.domain_name}"
  type = "A"

  alias {
    name = "${aws_alb.main.dns_name}"
    zone_id = "${aws_alb.main.zone_id}"
    evaluate_target_health = false
  }
}