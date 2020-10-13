
output "accounts_dns" {
  value = "${module.accounts.discovery_dns}"
}

output "authorization_server_dns" {
  value = "${module.authorization_server.discovery_dns}"
}

output "customer_service_dns" {
  value = "${module.customer_service.discovery_dns}"
}

output "library_dns" {
  value = "${module.library.discovery_dns}"
}

output "payments_dns" {
  value = "${module.payments.discovery_dns}"
}