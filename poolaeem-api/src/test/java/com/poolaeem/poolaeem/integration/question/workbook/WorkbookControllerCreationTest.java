package com.poolaeem.poolaeem.integration.question.workbook;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import com.poolaeem.poolaeem.question.presentation.dto.WorkbookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문제집 관리 테스트")
@Sql(scripts = "classpath:/sql/question/workbook.sql")
class WorkbookControllerCreationTest extends BaseIntegrationTest {
    private final String CREATE_WORKBOOK = "/api/workbook";

    @Autowired
    private WorkbookRepository workbookRepository;

    @Test
    @DisplayName("문제집을 생성할 수 있다.")
    void testCreateWorkbook() throws Exception {
        String name = "새로만든_문제집_기존에는_없음";
        String description = "문제집 설명글";
        WorkbookTheme theme = WorkbookTheme.PINK;

        assertThat(workbookRepository.findAll().stream().filter(workbook -> workbook.getName().equals(name)).count()).isZero();

        ResultActions result = this.mockMvc.perform(
                post(CREATE_WORKBOOK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookCreateDto(
                                name,
                                description,
                                WorkbookTheme.PINK
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        assertThat(workbookRepository.findAll().stream().filter(workbook -> workbook.getName().equals(name)).count()).isEqualTo(1);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 31})
    @DisplayName("문제집 이름은 1자 이상 30자 이하로만 생성할 수 있다.")
    void testCreateWorkbookForNameLengthValidation(int length) throws Exception {
        String name = TextGenerator.generate(length);
        String description = "문제집 설명글";
        WorkbookTheme theme = WorkbookTheme.PINK;

        ResultActions result = this.mockMvc.perform(
                post(CREATE_WORKBOOK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookCreateDto(
                                name,
                                description,
                                theme
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(BadRequestDataException.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {301})
    @DisplayName("문제집 설명은 0자 이상 300자 이하로만 생성할 수 있다.")
    void testCreateWorkbookForDescriptionLengthValidation(int length) throws Exception {
        String name = "문제집1";
        String description = TextGenerator.generate(length);
        WorkbookTheme theme = WorkbookTheme.PINK;

        ResultActions result = this.mockMvc.perform(
                post(CREATE_WORKBOOK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookCreateDto(
                                name,
                                description,
                                theme
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(BadRequestDataException.class));
    }

    @Test
    @DisplayName("문제집 생성 시 마지막 문제집의 다음 순서로 생성된다.")
    void testCreateWorkbookForOrder() throws Exception {
        String name = "마지막 순서 다음 순서로 생성된다.";
        String description = "description";
        WorkbookTheme theme = WorkbookTheme.PINK;

        Integer lastOrder = workbookRepository.findAll().stream()
                .filter(workbook -> workbook.getUserId().equals("user-1"))
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(Workbook::getOrder)
                .findFirst().orElse(0);

        this.mockMvc.perform(
                        post(CREATE_WORKBOOK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookCreateDto(
                                        name,
                                        description,
                                        theme
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        Integer currentOrder = workbookRepository.findAll().stream()
                .filter(workbook -> workbook.getUserId().equals("user-1"))
                .map(Workbook::getOrder)
                .mapToInt(Integer::intValue)
                .max().orElse(0);
        assertThat(currentOrder).isEqualTo(lastOrder + 1);
    }

    @Test
    @DisplayName("문제집 테마가 null일 경우 기본값인 PINK로 새로 생성된다.")
    void testCreateWorkbookForNullTheme() throws Exception {
        String name = "마지막 순서 다음 순서로 생성된다.";
        String description = "description";
        WorkbookTheme theme = null;

        this.mockMvc.perform(
                        post(CREATE_WORKBOOK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookCreateDto(
                                        name,
                                        description,
                                        theme
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        Workbook entity = workbookRepository.findAll().stream()
                .filter(workbook -> workbook.getUserId().equals("user-1"))
                .max((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .get();
        assertThat(entity.getTheme()).isEqualTo(WorkbookTheme.PINK);
    }
}