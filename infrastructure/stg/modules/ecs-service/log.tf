resource "aws_cloudwatch_log_group" "application_log_group" {
  name = "/ecs/${var.application_name}-app"
  retention_in_days = 30

  tags {
    Name = "${var.application_name}-log-group"
  }
}

resource "aws_cloudwatch_log_stream" "application_log_stream" {
  name = "${var.application_name}-log-stream"
  log_group_name = "${aws_cloudwatch_log_group.application_log_group.name}"

  depends_on = [
    "aws_cloudwatch_log_group.application_log_group"
  ]
}
