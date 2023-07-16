package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookAuthor;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;
import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentFormatGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 문제 풀이 컨트롤러 테스트")
class SolveControllerTest extends ApiDocumentationTest {
    private final String READ_WORKBOOK_INFO = "/api/workbooks/{workbookId}/info/solve";

    @Test
    @DisplayName("풀 문제의 정보를 조회할 수 있다.")
    void testSolveReadWorkbookInfo() throws Exception {
        String workbookId = "workbook-id";

        given(solveService.readSolveInfo(workbookId))
                .willReturn(new WorkbookSolveDto.SolveInfoRead(
                        workbookId,
                        "문제집1",
                        "기초 영단어 문제 모음",
                        new WorkbookAuthor(
                                "원작자",
                                "https://image.poolaeem.com/test/profile/1234"
                        ),
                        ZonedDateTime.of(LocalDateTime.of(2023, 7, 16, 16, 16, 30), ZoneId.of(ZoneOffset.UTC.getId())),
                        30,
                        12
                ));

        ResultActions result = this.mockMvc.perform(
                get(READ_WORKBOOK_INFO, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("solve/question/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("문제집 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("문제집 설명글"),
                                fieldWithPath("author.name").type(JsonFieldType.STRING).description("문제집 만든이"),
                                fieldWithPath("author.profileImageUrl").type(JsonFieldType.STRING).description("만든이 프로필 이미지 주소"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateTimeFormat()).description("문제집 생성일"),
                                fieldWithPath("problemCount").type(JsonFieldType.NUMBER).description("문제집의 문항수"),
                                fieldWithPath("solvedCount").type(JsonFieldType.NUMBER).description("문제집을 풀이한 수")
                        )
                ));
    }
}