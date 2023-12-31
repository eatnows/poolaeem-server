package com.poolaeem.poolaeem.file.application;

import com.poolaeem.poolaeem.common.file.FilePath;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void uploadNewFile(String fileId, FilePath path, MultipartFile fileObject);

    void deleteUploadedFile(String fileId, FilePath path);

    String getImageUrl(String fileId);
}
