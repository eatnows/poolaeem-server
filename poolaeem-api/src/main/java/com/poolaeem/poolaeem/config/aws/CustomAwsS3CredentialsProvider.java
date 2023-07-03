package com.poolaeem.poolaeem.config.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class CustomAwsS3CredentialsProvider implements AwsCredentialsProvider {

    private final String accessKey;
    private final String secretKey;

    public CustomAwsS3CredentialsProvider(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }
}
