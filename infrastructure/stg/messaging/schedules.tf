resource "aws_cloudwatch_event_rule" "scheduling_next_payments" {
  name = "scheduling-next-payments"
  schedule_expression = "cron(0 1 * * ? *)"
}

resource "aws_cloudwatch_event_target" "sns" {
  rule = "${aws_cloudwatch_event_rule.scheduling_next_payments.name}"
  target_id = "SendToSNS"
  arn = "${aws_sns_topic.scheduling_next_payments.arn}"
}

############################

resource "aws_cloudwatch_event_rule" "charge_payments" {
  name = "charge-payments"
  schedule_expression = "cron(0 9-18/3 * * ? *)"
}

resource "aws_cloudwatch_event_target" "charge_payments_target" {
  rule = "${aws_cloudwatch_event_rule.charge_payments.name}"
  target_id = "SendToSNS"
  arn = "${aws_sns_topic.charge_payments.arn}"
}
