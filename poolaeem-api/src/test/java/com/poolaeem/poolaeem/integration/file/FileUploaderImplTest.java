package com.poolaeem.poolaeem.integration.file;

import com.poolaeem.poolaeem.file.application.FileUploader;
import com.poolaeem.poolaeem.integration.base.BaseLocalStackTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("통합: 파일 업로드 테스트")
class FileUploaderImplTest extends BaseLocalStackTest {

    @Autowired
    private FileUploader fileUploader;

    @Test
    @DisplayName("파일을 S3에 업로드할 수 있다.")
    void uploadFile() {
        MockMultipartFile file = new MockMultipartFile("file_name.txt", "<< file content >>".getBytes(StandardCharsets.UTF_8));

        fileUploader.upload(file);
    }
}