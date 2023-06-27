package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.common.exception.file.FIleUploadException;
import com.poolaeem.poolaeem.file.application.FileUploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class FileUploaderImpl implements FileUploader {

    private final String BUCKET_NAME;

    private final S3Client s3Client;

    public FileUploaderImpl(@Value("${poolaeem.aws.s3.bucket-name}") String bucketName, S3Client s3Client) {
        this.BUCKET_NAME = bucketName;
        this.s3Client = s3Client;
    }

    @Override
    public void upload(MultipartFile file) {
        try {
            PutObjectRequest request = getPutobjectRequest(file);
            RequestBody requestBody = getRequestBody(file, request);

            s3Client.putObject(request, requestBody);
        } catch (IOException e) {
            throw new FIleUploadException();
        }
    }

    private RequestBody getRequestBody(MultipartFile file, PutObjectRequest request) throws IOException {
        return RequestBody.fromInputStream(file.getInputStream(), request.contentLength());
    }

    private PutObjectRequest getPutobjectRequest(MultipartFile file) throws IOException {
        return PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("image/" + file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentLength(file.getResource().contentLength())
                .acl(ObjectCannedACL.PRIVATE)
                .build();
    }
}
