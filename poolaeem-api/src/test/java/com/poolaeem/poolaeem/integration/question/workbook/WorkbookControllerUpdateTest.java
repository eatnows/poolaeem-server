package com.poolaeem.poolaeem.integration.question.workbook;

import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import com.poolaeem.poolaeem.question.presentation.dto.WorkbookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문제집 통합 테스트")
@Sql(scripts = "classpath:/sql/question/workbook.sql")
class WorkbookControllerUpdateTest extends BaseIntegrationTest {
    private final String UPDATE_WORKBOOK = "/api/workbooks/{workbookId}";

    @Autowired
    private WorkbookRepository workbookRepository;

    @Test
    @DisplayName("문제집의 정보를 수정할 수 있다.")
    void testUpdateWorkbookInfo() throws Exception {
        String workbookId = "workbook-1";
        String newName = "고등영어 개정판";
        String newDescription = "새롭게 개정된 고등영어로 업데이트하였습니다.";

        Workbook before = workbookRepository.findByIdAndIsDeletedFalse(workbookId).get();
        assertThat(before.getName()).isNotEqualTo(newName);
        assertThat(before.getDescription()).isNotEqualTo(newDescription);

        this.mockMvc.perform(
                        put(UPDATE_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookUpdateDto(
                                        newName, newDescription
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        Workbook after = workbookRepository.findByIdAndIsDeletedFalse(workbookId).get();
        assertThat(after.getName()).isEqualTo(newName);
        assertThat(after.getDescription()).isEqualTo(newDescription);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 31})
    @DisplayName("문제집의 이릉은 1자 이상 30자 이하로만 수정할 수 있다.")
    void testUpdateWorkbookForNameLength(int length) throws Exception {
        String workbookId = "workbook-1";
        String newName = TextGenerator.generate(length);
        String newDescription = "새롭게 개정된 고등영어로 업데이트하였습니다.";

        this.mockMvc.perform(
                        put(UPDATE_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookUpdateDto(
                                        newName, newDescription
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
                .andExpect(jsonPath("$.code", is(ApiResponseCode.REQUEST_VALIDATION.getCode())));
    }

    @ParameterizedTest
    @ValueSource(ints = {301})
    @DisplayName("문제집의 이릉은 0자 이상 301자 이하로만 수정할 수 있다.")
    void testUpdateWorkbookForDescriptLength(int length) throws Exception {
        String workbookId = "workbook-1";
        String newName = "고등영어 개정판";
        String newDescription = TextGenerator.generate(length);

        this.mockMvc.perform(
                        put(UPDATE_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookUpdateDto(
                                        newName, newDescription
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
                .andExpect(jsonPath("$.code", is(ApiResponseCode.REQUEST_VALIDATION.getCode())));
    }

    @Test
    @DisplayName("문제집 관리자가 아니면 문제집을 수정할 수 없다")
    void testUpdateWorkbookInByOtherUser() throws Exception {
        String workbookId = "workbook-2";
        String newName = "고등영어 개정판";
        String newDescription = "새롭게 개정된 고등영어로 업데이트하였습니다.";

        Workbook before = workbookRepository.findByIdAndIsDeletedFalse(workbookId).get();
        assertThat(before.getName()).isNotEqualTo(newName);
        assertThat(before.getDescription()).isNotEqualTo(newDescription);

        this.mockMvc.perform(
                        put(UPDATE_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookUpdateDto(
                                        newName, newDescription
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(ForbiddenRequestException.class))
                .andExpect(jsonPath("$.code", is(ApiResponseCode.FORBIDDEN.getCode())));

        Workbook after = workbookRepository.findByIdAndIsDeletedFalse(workbookId).get();
        assertThat(after.getName()).isNotEqualTo(newName);
        assertThat(after.getDescription()).isNotEqualTo(newDescription);
    }
}