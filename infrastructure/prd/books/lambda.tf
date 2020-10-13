locals {
  function_name = "edge-lambda"
  lambda_log_group_arn = "arn:${data.aws_partition.current.partition}:logs:*:${data.aws_caller_identity.current.account_id}:log-group:/aws/lambda/${local.function_name}"
  lambda_edge_log_group_arn = "arn:${data.aws_partition.current.partition}:logs:*:${data.aws_caller_identity.current.account_id}:log-group:/aws/lambda/us-east-1.${local.function_name}"
  log_group_arns = [
    "${list(local.lambda_log_group_arn, local.lambda_edge_log_group_arn)}"
  ]
}

resource "aws_lambda_function" "edge_lambda" {
  function_name = "${local.function_name}"
  filename = "function.zip"
  source_code_hash = "${filebase64sha256("${path.module}/function.zip")}"
  handler = "index.handler"
  runtime = "nodejs8.10"
  publish = "true"
  role = "${aws_iam_role.lambda_edge_role.arn}"

  tags {
    Environment = "prd"
  }

  depends_on = [
    "data.archive_file.lambda_zip"
  ]
}

resource "aws_iam_role" "lambda_edge_role" {
  name = "${local.function_name}"
  assume_role_policy = "${data.aws_iam_policy_document.assume_role.json}"

  tags {
    Environment = "prd"
  }
}

resource "aws_iam_policy" "logs" {
  name = "${local.function_name}-logs"
  policy = "${data.aws_iam_policy_document.logs.json}"
}

resource "aws_iam_policy_attachment" "logs" {
  name = "${local.function_name}-logs"
  roles = [
    "${aws_iam_role.lambda_edge_role.name}"
  ]
  policy_arn = "${aws_iam_policy.logs.arn}"
}

data "archive_file" "lambda_zip" {
  type = "zip"
  source_file = "${path.module}/index.js"
  output_path = "${path.module}/function.zip"
}

data "aws_iam_policy_document" "assume_role" {
  statement {
    effect = "Allow"
    actions = [
      "sts:AssumeRole"
    ]

    principals {
      type = "Service"
      identifiers = [
        "lambda.amazonaws.com",
        "edgelambda.amazonaws.com"
      ]
    }
  }
}

data "aws_iam_policy_document" "logs" {
  statement {
    effect = "Allow"

    actions = [
      "logs:CreateLogGroup",
    ]

    resources = [
      "*",
    ]
  }

  statement {
    effect = "Allow"

    actions = [
      "logs:CreateLogStream",
      "logs:PutLogEvents",
    ]

    resources = [
      "${concat(formatlist("%v:*", local.log_group_arns), formatlist("%v:*:*", local.log_group_arns))}"
    ]
  }
}

data "aws_partition" "current" {}

data "aws_caller_identity" "current" {}