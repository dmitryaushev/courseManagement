package com.courses.management.common;

import com.amazonaws.services.s3.AmazonS3;

public interface AWSService {

    AmazonS3 getS3Client();
    String getS3BucketName();
}
