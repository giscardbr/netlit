resource "aws_alb" "main" {
  name = "netlit-load-balancer"
  subnets = [
    "${aws_subnet.public.*.id}"]
  security_groups = [
    "${aws_security_group.lb.id}"]
}

resource "aws_alb_listener" "front_end" {
  load_balancer_arn = "${aws_alb.main.id}"
  port = "${var.public_port}"
  protocol = "HTTPS"
  ssl_policy = "ELBSecurityPolicy-2016-08"

  certificate_arn = "${data.aws_acm_certificate.netlit_com_br_certificate.arn}"

  default_action {
    type = "fixed-response"

    fixed_response {
      content_type = "application/json"
      message_body = "{\"error\":\"Not Found\"}"
      status_code = "404"
    }
  }
}