resource "aws_ecs_task_definition" "app" {
  family = "${var.application_name}-app-task"
  execution_role_arn = "${data.aws_iam_role.execution_role.arn}"
  network_mode = "awsvpc"
  task_role_arn = "${var.task_role}"
  requires_compatibilities = [
    "FARGATE"
  ]
  cpu = "${var.cpu}"
  memory = "${var.memory}"
  container_definitions = "${file("${path.root}/${var.application_name}-task-definition.json")}"

  depends_on = [
    "local_file.task_definition"
  ]
}

resource "local_file" "task_definition" {
  content = "${data.template_file.application.rendered}"
  filename = "${path.root}/${var.application_name}-task-definition.json"
}

data "template_file" "application" {
  template = "${file("${path.module}/task-definition.tpl")}"

  vars {
    app_name = "${var.application_name}-app"
    app_image = "${var.repository_url}:${var.container_version}"
    fargate_cpu = "${var.cpu}"
    fargate_memory = "${var.memory}"
    log_group_name = "${aws_cloudwatch_log_group.application_log_group.name}"
    aws_region = "${var.aws_region}"
    app_port = "${var.container_port}"
    environment_variables = "${replace(replace(var.environment_variables,"\n","")," ","")}"
  }
}


data "aws_iam_role" "execution_role" {
  name = "ecsTaskExecutionRole"
}