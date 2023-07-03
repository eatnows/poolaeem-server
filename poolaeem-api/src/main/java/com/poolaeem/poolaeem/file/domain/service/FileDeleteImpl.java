package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.file.application.FileDelete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
public class FileDeleteImpl implements FileDelete {

    private final String BUCKET_NAME;

    private final S3Client s3Client;

    public FileDeleteImpl(@Value("${poolaeem.aws.s3.bucket-name}") String bucketName, S3Client s3Client) {
        this.BUCKET_NAME = bucketName;
        this.s3Client = s3Client;
    }

    @Override
    public void deleteUploadedFile(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build());
    }
}
