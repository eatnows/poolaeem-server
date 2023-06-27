package com.poolaeem.poolaeem.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

@Configuration
public class S3AwsCredentialsProviderConfiguration {

    private final String accessKey;
    private final String secretKey;
    private final CustomAwsS3CredentialsProvider s3CredentialsProvider;

    public S3AwsCredentialsProviderConfiguration(@Value("${poolaeem.aws.s3.access-key}") String accessKey, @Value("${poolaeem.aws.s3.secret-key}") String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.s3CredentialsProvider = new CustomAwsS3CredentialsProvider(accessKey, secretKey);
    }

    @Bean
    public AwsCredentialsProvider s3CredentialsProvider() {
        return s3CredentialsProvider;
    }
}
