resource "aws_db_instance" "mysql" {
  engine = "mysql"
  engine_version = "5.7"
  instance_class = "db.t2.micro"
  name = "library"
  username = "ARd4FmUTZbt3CXaJ"
  password = "w8PKJzE2axANmk4s"
  copy_tags_to_snapshot = true
  publicly_accessible = true
  skip_final_snapshot = true

  tags {
    Environment = "${var.profile_environment}"
  }
}

resource "aws_security_group" "mysql" {
  name = "rds-launch-wizard"
  description = "Created from the RDS Management Console: 2019/05/31 18:19:39"
  vpc_id = "${aws_vpc.main.id}"

  tags {
    Environment = "${var.profile_environment}"
  }
}

############################

resource "aws_db_instance" "payments_db" {
  identifier = "payments"
  engine = "mysql"
  engine_version = "5.7"
  instance_class = "db.t2.micro"
  storage_type = "gp2"
  name = "payments"
  username = "KqwU76juxEcXtCdf"
  password = "AJT8EPYcNGxMqpKm"
  parameter_group_name = "default.mysql5.7"
  copy_tags_to_snapshot = true
  publicly_accessible = true
  skip_final_snapshot = true
  allocated_storage = 20
  db_subnet_group_name = "${aws_db_subnet_group.default.id}"
  vpc_security_group_ids = [
    "${aws_security_group.payments_db.id}"
  ]


  tags {
    Environment = "${var.profile_environment}"
  }
}

resource "aws_db_subnet_group" "default" {
  name = "db_subnet_group"
  description = "Database group of subnets"
  subnet_ids = [
    "${aws_subnet.public.*.id}"
  ]
}

resource "aws_security_group" "payments_db" {
  name = "payments_db_sg"
  description = "Allow all inbound traffic"
  vpc_id = "${aws_vpc.main.id}"

  ingress {
    from_port = 3306
    to_port = 3306
    protocol = "TCP"
    cidr_blocks = [
      "0.0.0.0/0"
    ]
  }

  tags {
    Environment = "${var.profile_environment}"
  }
}