data "aws_route53_zone" "netlit_com_br" {
  name = "${var.domain_name}"
  private_zone = false
}

resource "aws_route53_record" "book" {
  zone_id = "${data.aws_route53_zone.netlit_com_br.zone_id}"
  name = "book.${var.domain_name}"
  type = "A"

  alias {
    name = "${aws_cloudfront_distribution.netlit_books.domain_name}"
    zone_id = "${aws_cloudfront_distribution.netlit_books.hosted_zone_id}"
    evaluate_target_health = false
  }
}