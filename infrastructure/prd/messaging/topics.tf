resource "aws_sns_topic" "credentials_created" {
  name = "credentials-created-topic"
}

resource "aws_sns_topic_subscription" "credentials_created_to_authorization_server_queue" {
  topic_arn = "${aws_sns_topic.credentials_created.arn}"
  protocol = "sqs"
  endpoint = "${aws_sqs_queue.authorization_server_queue.arn}"

  depends_on = [
    "aws_sns_topic.credentials_created"
  ]
}

############################

resource "aws_sns_topic" "credentials_updated" {
  name = "credentials-updated-topic"
}

resource "aws_sns_topic_subscription" "credentials_updated_to_authorization_server_queue" {
  topic_arn = "${aws_sns_topic.credentials_updated.arn}"
  protocol = "sqs"
  endpoint = "${aws_sqs_queue.authorization_server_queue.arn}"

  depends_on = [
    "aws_sns_topic.credentials_updated"
  ]
}

############################

resource "aws_sns_topic" "credit_card_registration" {
  name = "credit-card-registration-topic"
}

resource "aws_sns_topic_subscription" "credit_card_registration_to_payments_queue" {
  topic_arn = "${aws_sns_topic.credit_card_registration.arn}"
  protocol = "sqs"
  endpoint = "${aws_sqs_queue.payments_queue.arn}"

  depends_on = [
    "aws_sns_topic.credit_card_registration"
  ]
}

############################

resource "aws_sns_topic" "scheduling_next_payments" {
  name = "scheduling-next-payments-topic"
}

resource "aws_sns_topic_subscription" "scheduling_next_payments_to_payments_queue" {
  topic_arn = "${aws_sns_topic.scheduling_next_payments.arn}"
  protocol = "sqs"
  endpoint = "${aws_sqs_queue.payments_queue.arn}"

  depends_on = [
    "aws_sns_topic.scheduling_next_payments"
  ]
}

resource "aws_sns_topic_policy" "default" {
  arn = "${aws_sns_topic.scheduling_next_payments.arn}"
  policy = "${data.aws_iam_policy_document.sns_topic_policy.json}"
}

data "aws_iam_policy_document" "sns_topic_policy" {
  statement {
    effect = "Allow"
    actions = [
      "SNS:Publish"
    ]

    principals {
      type = "Service"
      identifiers = [
        "events.amazonaws.com"
      ]
    }

    resources = [
      "${aws_sns_topic.scheduling_next_payments.arn}"
    ]
  }
}

############################

resource "aws_sns_topic" "charge_payments" {
  name = "charge-payments-topic"
}

resource "aws_sns_topic_subscription" "charge_payments_to_payments_queue" {
  topic_arn = "${aws_sns_topic.charge_payments.arn}"
  protocol = "sqs"
  endpoint = "${aws_sqs_queue.payments_queue.arn}"

  depends_on = [
    "aws_sns_topic.charge_payments"
  ]
}

resource "aws_sns_topic_policy" "charge_payments_policy" {
  arn = "${aws_sns_topic.charge_payments.arn}"
  policy = "${data.aws_iam_policy_document.charge_payments_topic_policy.json}"
}

data "aws_iam_policy_document" "charge_payments_topic_policy" {
  statement {
    effect = "Allow"
    actions = [
      "SNS:Publish"
    ]

    principals {
      type = "Service"
      identifiers = [
        "events.amazonaws.com"
      ]
    }

    resources = [
      "${aws_sns_topic.charge_payments.arn}"
    ]
  }
}
