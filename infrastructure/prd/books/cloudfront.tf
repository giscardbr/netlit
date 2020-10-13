resource "aws_cloudfront_distribution" "netlit_books" {

  origin {
    domain_name = "${aws_s3_bucket.netlit_books.bucket}.s3.amazonaws.com"
    origin_id = "netlit-books-origin"

    s3_origin_config {
      origin_access_identity = "${aws_cloudfront_origin_access_identity.origin_access_identity.cloudfront_access_identity_path}"
    }
  }

  enabled = true
  default_root_object = "index.html"
  price_class = "PriceClass_200"

  default_cache_behavior {

    allowed_methods = [
      "GET",
      "HEAD",
      "OPTIONS"
    ]

    cached_methods = [
      "GET",
      "HEAD",
      "OPTIONS"
    ]

    target_origin_id = "netlit-books-origin"

    forwarded_values {
      query_string = true

      cookies {
        forward = "none"
      }

      headers = [
        "Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers"
      ]
    }

    lambda_function_association {
      event_type = "viewer-request"
      lambda_arn = "${aws_lambda_function.edge_lambda.qualified_arn}"
    }

    viewer_protocol_policy = "allow-all"
    min_ttl = 0
    default_ttl = 3600
    max_ttl = 86400
    compress = true
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  restrictions {

    geo_restriction {
      restriction_type = "none"
    }
  }

  tags {
    Environment = "prd"
  }
}