package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.file.application.FileDelete;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@DisplayName("단위: S3에 업로드된 파일 삭제 테스트")
class FileDeleteImplTest {

    @InjectMocks
    private FileDeleteImpl fileDelete;

    @Mock
    private S3Client s3Client;

    @Test
    void testDeleteUploadedFile() {
        String key = "images/profile/poolaeem_logo.png";
        fileDelete.deleteUploadedFile(key);

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}