output "vpc_id" {
  value = "${aws_vpc.main.id}"
}

output "load_balancer_listener_arn" {
  value = "${aws_alb_listener.front_end.arn}"
}

output "load_balancer_hostname" {
  value = "${aws_alb.main.dns_name}"
}

output "ecr_url_authorization_server" {
  value = "${module.authorization_server_repository.ecr_url}"
}

output "ecr_url_accounts" {
  value = "${module.accounts_repository.ecr_url}"
}

output "ecr_url_payments" {
  value = "${module.payments_repository.ecr_url}"
}

output "ecr_url_library" {
  value = "${module.library_repository.ecr_url}"
}

output "ecr_url_customer_service" {
  value = "${module.customer_service_repository.ecr_url}"
}

output "dns_namespace_id" {
  value = "${aws_service_discovery_private_dns_namespace.netlit_dns_namespace.id}"
}