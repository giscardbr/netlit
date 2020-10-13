output "credentials_created_sns_topic_arn" {
  value = "${aws_sns_topic.credentials_created.arn}"
}

output "credentials_updated_sns_topic_arn" {
  value = "${aws_sns_topic.credentials_updated.arn}"
}

output "credit_card_registration_sns_topic_arn" {
  value = "${aws_sns_topic.credit_card_registration.arn}"
}

output "scheduling_next_payments_sns_topic_arn" {
  value = "${aws_sns_topic.scheduling_next_payments.arn}"
}

output "authorization_server_queue_url" {
  value = "${aws_sqs_queue.authorization_server_queue.id}"
}