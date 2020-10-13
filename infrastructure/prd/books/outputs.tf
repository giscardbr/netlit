output "s3_static_website_endpoint" {
  value = "http://${aws_s3_bucket.netlit_books.bucket}.s3-website-${aws_s3_bucket.netlit_books.region}.amazonaws.com"
}