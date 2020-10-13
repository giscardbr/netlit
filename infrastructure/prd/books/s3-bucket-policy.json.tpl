{
  "Statement": [
    {
      "Action": "s3:GetObject",
      "Effect": "Allow",
      "Principal": {
        "AWS": "${s3_canonical_user}"
      },
      "Resource": "arn:aws:s3:::${bucket_name}/*",
      "Sid": "PrivateReadForGetBucketObjects"
    }
  ],
  "Version": "2012-10-17"
}