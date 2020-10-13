resource "aws_alb_target_group" "app" {
  name = "${var.application_name}"
  port = 80
  protocol = "HTTP"
  vpc_id = "${var.vpc}"
  target_type = "ip"

  health_check {
    healthy_threshold = "3"
    interval = "30"
    protocol = "HTTP"
    matcher = "200"
    timeout = "3"
    path = "${var.health_check_path}"
    unhealthy_threshold = "2"
  }
}

resource "aws_lb_listener_rule" "container" {
  count = "${length(var.exposed_resources)}"
  listener_arn = "${data.aws_alb_listener.listener.arn}"

  condition {
    field = "path-pattern"
    values = ["${element(var.exposed_resources, count.index)}"]
  }

  action {
    type = "forward"
    target_group_arn = "${aws_alb_target_group.app.id}"
  }
}

data "aws_alb_listener" "listener" {
  arn = "${var.load_balancer_listener}"
}
