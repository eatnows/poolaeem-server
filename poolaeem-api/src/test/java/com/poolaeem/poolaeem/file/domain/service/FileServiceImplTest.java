package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.common.file.FilePath;
import com.poolaeem.poolaeem.file.application.FileDelete;
import com.poolaeem.poolaeem.file.application.FileUpload;
import com.poolaeem.poolaeem.file.domain.entity.File;
import com.poolaeem.poolaeem.file.infra.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 파일 관리 테스트")
class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileUpload fileUpload;
    @Mock
    private FileDelete fileDelete;

    @Test
    @DisplayName("파일 업로드 시 file entity 를 새로 저장한다.")
    void testSaveFileEntity() {
        String fileId = "file-id";
        FilePath filePath = FilePath.PROFILE_IMAGE;
        MockMultipartFile mockFile = new MockMultipartFile("file", "file_name.txt", "text/plain", "<< file content >>".getBytes(StandardCharsets.UTF_8));

        fileService.uploadNewFile(fileId, filePath, mockFile);

        verify(fileRepository, times(1)).save(any());
        verify(fileUpload, times(1)).upload(any(), any());
        verify(fileDelete, times(0)).deleteUploadedFile(anyString());
    }

    @Test
    @DisplayName("파일 삭제 시 file entity 를 완전 삭제한다")
    void testDeleteFileEntity() {
        String fileId = "file-id";
        FilePath filePath = FilePath.PROFILE_IMAGE;

        File mockFile = new File(fileId, "filename", 100, filePath.getPath());

        given(fileRepository.findByIdAndIsDeletedFalse(fileId))
                .willReturn(Optional.of(mockFile));

        assertThat(mockFile.getIsDeleted()).isFalse();
        fileService.deleteUploadedFile(fileId, filePath);

        assertThat(mockFile.getIsDeleted()).isTrue();
        verify(fileDelete, times(1)).deleteUploadedFile(anyString());
        verify(fileUpload, times(0)).upload(any(), any());
    }
}