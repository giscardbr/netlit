resource "aws_alb" "main" {
  name = "netlit-load-balancer"
  subnets = [
    "${aws_subnet.public.*.id}"
  ]
  security_groups = [
    "${aws_security_group.lb.id}"
  ]

  tags {
    Environment = "${var.profile_environment}"
  }
}

resource "aws_alb_listener" "front_end" {
  load_balancer_arn = "${aws_alb.main.id}"
  port = "${var.public_port}"
  protocol = "HTTP"

  default_action {
    type = "fixed-response"

    fixed_response {
      content_type = "application/json"
      message_body = "{\"error\":\"Not Found\"}"
      status_code = "404"
    }
  }
}