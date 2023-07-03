package com.poolaeem.poolaeem.integration.file;

import com.poolaeem.poolaeem.file.application.FileUpload;
import com.poolaeem.poolaeem.file.domain.entity.File;
import com.poolaeem.poolaeem.common.file.FilePath;
import com.poolaeem.poolaeem.integration.base.BaseLocalStackTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@DisplayName("통합: 파일 업로드 테스트")
class FileUploadImplTest extends BaseLocalStackTest {

    @Autowired
    private FileUpload fileUpload;

    @Test
    @DisplayName("파일을 S3에 업로드할 수 있다.")
    void uploadFile() {
        MockMultipartFile fileObject = new MockMultipartFile("file_name.txt", "file_name.txt", "text/plain", "<< file content >>".getBytes(StandardCharsets.UTF_8));
        File fileEntity = new File(UUID.randomUUID().toString(), FilePath.PROFILE_IMAGE, fileObject);

        fileUpload.upload(fileObject, fileEntity);
    }
}