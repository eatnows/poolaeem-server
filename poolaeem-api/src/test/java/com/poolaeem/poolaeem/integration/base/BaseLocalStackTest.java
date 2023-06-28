package com.poolaeem.poolaeem.integration.base;

import com.poolaeem.poolaeem.integration.config.LocalStackS3Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;


@Disabled
@Import(value = LocalStackS3Config.class)
public class BaseLocalStackTest extends BaseIntegrationTest {

    @Value("${poolaeem.aws.s3.bucket-name}")
    private final String BUCKET_NAME = null;

    @Autowired
    private S3Client s3Client;

    @BeforeEach
    public void beforeEach() {
        createBucket();
    }

    protected void createBucket() {
        if (s3Client.listBuckets().buckets().size() > 0) return;
        s3Client.createBucket(CreateBucketRequest.builder()
                .bucket(BUCKET_NAME)
                .build());
    }
}
