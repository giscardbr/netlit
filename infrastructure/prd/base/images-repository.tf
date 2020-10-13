module "authorization_server_repository" {
  source = "../modules/ecr-repository"

  application_name = "authorization-server"
}

module "accounts_repository" {
  source = "../modules/ecr-repository"

  application_name = "accounts"
}

module "payments_repository" {
  source = "../modules/ecr-repository"

  application_name = "payments"
}

module "library_repository" {
  source = "../modules/ecr-repository"

  application_name = "library"
}

module "customer_service_repository" {
  source = "../modules/ecr-repository"

  application_name = "customer-service"
}