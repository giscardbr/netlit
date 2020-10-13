resource "aws_service_discovery_service" "service_discovery" {
  name = "${var.application_name}"

  dns_config {
    namespace_id = "${var.dns_namespace_id}"
    routing_policy = "MULTIVALUE"

    dns_records {
      ttl = 10
      type = "A"
    }

    dns_records {
      ttl = 10
      type = "SRV"
    }

  }

  health_check_custom_config {
    failure_threshold = 5
  }
}
