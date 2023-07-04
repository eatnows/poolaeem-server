package com.poolaeem.poolaeem.workbook.domain.service;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.workbook.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.workbook.domain.entity.Workbook;
import com.poolaeem.poolaeem.workbook.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.workbook.infra.repository.WorkbookRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 문제집 관리 테스트")
class WorkbookServiceImplTest {
    @InjectMocks
    private WorkbookServiceImpl workbookService;
    @Mock
    private WorkbookRepository workbookRepository;

    @Test
    @DisplayName("문제집을 생성할 수 있다.")
    void testCreateWorkbook() {
        String userId = "user-1";
        String name = TextGenerator.generate(30);
        String description = "";

        workbookService.createWorkbook(userId, name, description);

        verify(workbookRepository, times(1)).save(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 31})
    @DisplayName("문제집의 이름은 1자이상 30자 이하까지만 설정할 수 있다.")
    void testCreateWorkbookForNameLength(int length) {
        String userId = "user-1";
        String name = TextGenerator.generate(length);
        String description = "";

        assertThatThrownBy(() -> workbookService.createWorkbook(userId, name, description))
                .isInstanceOf(BadRequestDataException.class);

        verify(workbookRepository, times(0)).save(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {301})
    @DisplayName("문제집의 설명글은 300자 이하까지만 설정할 수 있다.")
    void testCreateWorkbookForDescriptionLength(int length) {
        String userId = "user-1";
        String name = "문제집";
        String description = TextGenerator.generate(length);

        assertThatThrownBy(() -> workbookService.createWorkbook(userId, name, description))
                .isInstanceOf(BadRequestDataException.class);

        verify(workbookRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("문제집의 정보를 수정할 수 있다.")
    void testUpdateWorkbookInfo() {
        String workbookId = "workbook-id";
        String userId = "user-id";
        String newName = "문제집이름";
        String newDescription = "문제집 설명";
        WorkbookDto.WorkbookUpdateParam param = getWorkbookUpdateParam(workbookId, userId, newName, newDescription);

        Workbook workbook = getWorkbookEntitiy(userId);
        given(workbookRepository.findByIdAndIsDeletedFalse(workbookId))
                .willReturn(Optional.of(workbook));

        assertThat(workbook.getName()).isNotEqualTo(newName);
        assertThat(workbook.getDescription()).isNotEqualTo(newDescription);

        workbookService.updateWorkbook(param);

        assertThat(workbook.getName()).isEqualTo(newName);
        assertThat(workbook.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("문제집의 관리자와 요청한 유저가 다르면 수정할 수 없다.")
    void testUpdateWorkbookInfoByOtherUser() {
        String workbookId = "workbook-id";
        String userId = "user-id";
        String newName = "문제집이름";
        String newDescription = "문제집 설명";
        WorkbookDto.WorkbookUpdateParam param = getWorkbookUpdateParam(workbookId, userId, newName, newDescription);

        Workbook workbook = getWorkbookEntitiy("other-userId");
        given(workbookRepository.findByIdAndIsDeletedFalse(workbookId))
                .willReturn(Optional.of(workbook));

        assertThatThrownBy(() -> workbookService.updateWorkbook(param))
                .isInstanceOf(ForbiddenRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 31})
    @DisplayName("문제집의 이름은 1자 이상 30자 이하로만 수정할 수 있다.")
    void testUpdateWorkbookInfoForNameLength(int length) {
        String workbookId = "workbook-id";
        String userId = "user-id";
        String newName = TextGenerator.generate(length);
        String newDescription = "문제집 설명";
        WorkbookDto.WorkbookUpdateParam param = getWorkbookUpdateParam(workbookId, userId, newName, newDescription);

        assertThatThrownBy(() -> workbookService.updateWorkbook(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {301})
    @DisplayName("문제집의 설명은 0자 이상 300자 이하로만 수정할 수 있다.")
    void testUpdateWorkbookInfoForDescriptionLength(int length) {
        String workbookId = "workbook-id";
        String userId = "user-id";
        String newName = "문제집이름";
        String newDescription = TextGenerator.generate(length);
        WorkbookDto.WorkbookUpdateParam param = getWorkbookUpdateParam(workbookId, userId, newName, newDescription);

        assertThatThrownBy(() -> workbookService.updateWorkbook(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @NotNull
    private WorkbookDto.WorkbookUpdateParam getWorkbookUpdateParam(String workbookId, String userId, String newName, String newDescription) {
        return new WorkbookDto.WorkbookUpdateParam(
                workbookId,
                userId,
                newName,
                newDescription
        );
    }

    @NotNull
    private Workbook getWorkbookEntitiy(String userId) {
        return new Workbook(
                userId,
                "oldName",
                "oldDescription",
                WorkbookTheme.PINK,
                2
        );
    }
}