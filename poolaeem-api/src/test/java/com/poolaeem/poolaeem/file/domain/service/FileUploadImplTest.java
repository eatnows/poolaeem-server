package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.common.file.FilePath;
import com.poolaeem.poolaeem.file.domain.entity.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: S3 파일 업로드 테스트")
class FileUploadImplTest {

    @InjectMocks
    private FileUploadImpl fileUpload;

    @Mock
    private S3Client s3Client;

    @Test
    @DisplayName("파일을 S3에 업로드할 수 있다.")
    void testUploadFile() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "file_name.txt", "text/plain", "<< file content >>".getBytes(StandardCharsets.UTF_8));
        File file = new File(
                "file-id",
                FilePath.PROFILE_IMAGE,
                mockFile
        );

        fileUpload.upload(mockFile, file);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }
}