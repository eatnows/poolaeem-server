package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.common.exception.file.FIleUploadException;
import com.poolaeem.poolaeem.common.exception.file.FileNotFoundException;
import com.poolaeem.poolaeem.file.application.FileUpload;
import com.poolaeem.poolaeem.file.domain.entity.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class FileUploadImpl implements FileUpload {

    private final String BUCKET_NAME;

    private final S3Client s3Client;

    public FileUploadImpl(@Value("${poolaeem.aws.s3.bucket-name}") String bucketName, S3Client s3Client) {
        this.BUCKET_NAME = bucketName;
        this.s3Client = s3Client;
    }

    @Override
    public void upload(MultipartFile fileObject, File entity) {
        try {
            PutObjectRequest request = getPutObjectRequest(fileObject, entity);
            RequestBody requestBody = getRequestBody(fileObject, request);

            s3Client.putObject(request, requestBody);
        } catch (IOException e) {
            throw new FileNotFoundException();
        } catch (Exception e) {
            throw new FIleUploadException();
        }
    }

    private RequestBody getRequestBody(MultipartFile file, PutObjectRequest request) throws IOException {
        return RequestBody.fromInputStream(file.getInputStream(), request.contentLength());
    }

    private PutObjectRequest getPutObjectRequest(MultipartFile file, File entity) throws IOException {
        return PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(entity.getPath() + entity.getId())
                .contentType(file.getContentType())
                .contentLength(file.getResource().contentLength())
                .acl(ObjectCannedACL.PRIVATE)
                .build();
    }
}
