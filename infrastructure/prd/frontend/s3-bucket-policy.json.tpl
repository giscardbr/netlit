{
  "Statement": [
    {
      "Action": "s3:GetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Resource": "arn:aws:s3:::${bucket_name}/*",
      "Sid": "PublicReadForGetBucketObjects"
    }
  ],
  "Version": "2012-10-17"
}