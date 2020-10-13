resource "aws_sqs_queue" "authorization_server_queue" {
  name = "authorization-server-queue"

  max_message_size = 2048
  message_retention_seconds = 86400
  receive_wait_time_seconds = 10
}

resource "aws_sqs_queue_policy" "sqs_policy" {
  queue_url = "${aws_sqs_queue.authorization_server_queue.id}"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Id": "sqspolicy",
  "Statement": [
    {
      "Sid": "First",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "sqs:SendMessage",
      "Resource": "${aws_sqs_queue.authorization_server_queue.arn}",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "arn:aws:sns:*:*:*"
        }
      }
    }
  ]
}
POLICY
}

############################

resource "aws_sqs_queue" "payments_queue" {
  name = "payments-queue"

  max_message_size = 2048
  message_retention_seconds = 86400
  receive_wait_time_seconds = 10
}

resource "aws_sqs_queue_policy" "payments_queue_policy" {
  queue_url = "${aws_sqs_queue.payments_queue.id}"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Id": "sqspolicy",
  "Statement": [
    {
      "Sid": "First",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "sqs:SendMessage",
      "Resource": "${aws_sqs_queue.payments_queue.arn}",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "arn:aws:sns:*:*:*"
        }
      }
    }
  ]
}
POLICY
}

############################


###########
### SQS ###
###########

#Ref: https://www.terraform.io/docs/providers/aws/r/sqs_queue.html
resource "aws_sqs_queue" "sendmail" {
  name = "sendmail"

  max_message_size = 2048
  message_retention_seconds = 86400
  receive_wait_time_seconds = 10
}

resource "aws_sqs_queue_policy" "sqs_sendmail_policy" {
  queue_url = "${aws_sqs_queue.sendmail.id}"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Id": "sqspolicy",
  "Statement": [
    {
      "Sid": "First",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "sqs:SendMessage",
      "Resource": "${aws_sqs_queue.sendmail.arn}",
      "Condition": {
        "ArnEquals": {
          "aws:SourceArn": "arn:aws:sns:*:*:*"
        }
      }
    }
  ]
}
POLICY
}

###########
### SNS ###
###########

#Ref: https://www.terraform.io/docs/providers/aws/r/sns_topic.html
resource "aws_sns_topic" "sendmail_sns" {
  name = "sendmail-sns"
}

########################
### SNS Subscription ###
########################


#Ref: https://www.terraform.io/docs/providers/aws/r/sns_topic_subscription.html
resource "aws_sns_topic_subscription" "user_updates_sqs_target" {
  topic_arn = "${aws_sns_topic.sendmail_sns.arn}"
  protocol  = "sqs"
  endpoint  = "${aws_sqs_queue.sendmail.arn}"
}