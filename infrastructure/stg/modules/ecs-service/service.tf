resource "aws_ecs_service" "application" {
  name = "${var.application_name}"
  cluster = "${var.cluster}"
  task_definition = "${aws_ecs_task_definition.app.arn}"
  desired_count = "${var.replicas}"
  launch_type = "FARGATE"

  network_configuration {
    security_groups = [
      "${var.security_groups}"
    ]
    subnets = [
      "${data.aws_subnet_ids.privates.ids}"
    ]
    assign_public_ip = true
  }

  service_registries {
    registry_arn = "${aws_service_discovery_service.service_discovery.arn}"
    port = "${var.container_port}"
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.app.id}"
    container_name = "${var.application_name}-app"
    container_port = "${var.container_port}"
  }

  depends_on = [
    "aws_ecs_task_definition.app"
  ]
}

data "aws_subnet_ids" "privates" {
  vpc_id = "${var.vpc}"

  tags = {
    Tier = "Private"
  }
}
