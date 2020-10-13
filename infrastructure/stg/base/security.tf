# ALB Security Group: Edit this to restrict access to the application
resource "aws_security_group" "lb" {
  name = "netlit-load-balancer-security-group"
  description = "controls access to the ALB"
  vpc_id = "${aws_vpc.main.id}"

  ingress {
    protocol = "tcp"
    from_port = "${var.public_port}"
    to_port = "${var.public_port}"
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  egress {
    protocol = "-1"
    from_port = 0
    to_port = 0
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  tags {
    Environment = "${var.profile_environment}"
  }
}

# Traffic to the ECS cluster should only come from the ALB
resource "aws_security_group" "ecs_tasks" {
  name = "netlit-ecs-tasks-security-group"
  description = "allow inbound access from the ALB only"
  vpc_id = "${aws_vpc.main.id}"

  ingress {
    protocol = "tcp"
    from_port = "${var.app_port}"
    to_port = "${var.app_port}"
    security_groups = [
      "${aws_security_group.lb.id}"
    ],
    cidr_blocks = [
      "172.17.0.0/16"
    ]
  }

  egress {
    protocol = "-1"
    from_port = 0
    to_port = 0
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  tags {
    Environment = "${var.profile_environment}"
  }
}