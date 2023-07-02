package com.poolaeem.poolaeem.file.application;

import com.poolaeem.poolaeem.file.domain.entity.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileUpload {
    void upload(MultipartFile file, File entity);
}
