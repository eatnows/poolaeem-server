package com.poolaeem.poolaeem.workbook.domain.service;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.workbook.infra.repository.WorkbookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
}