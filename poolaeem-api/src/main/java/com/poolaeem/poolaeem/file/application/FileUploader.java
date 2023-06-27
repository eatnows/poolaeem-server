package com.poolaeem.poolaeem.file.application;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    void upload(MultipartFile file);
}
