output "ecr_url" {
  value = "${aws_ecr_repository.image_repository.repository_url}"
}