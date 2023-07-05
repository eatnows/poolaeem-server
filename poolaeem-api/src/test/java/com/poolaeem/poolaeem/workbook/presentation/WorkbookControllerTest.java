package com.poolaeem.poolaeem.workbook.presentation;

import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentLinkGenerator;
import com.poolaeem.poolaeem.workbook.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.workbook.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.workbook.presentation.dto.WorkbookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 문제집 컨트롤러 테스트")
class WorkbookControllerTest extends ApiDocumentationTest {
    private final String CREATE_WORKBOOK = "/api/workbook";
    private final String UPDATE_WORKBOOK = "/api/workbooks/{workbookId}";
    private final String READ_WORKBOOK_INFO = "/api/workbooks/{workbookId}";

    @Test
    @DisplayName("문제집을 생성할 수 있다")
    void testCreateWorkbook() throws Exception {
        String name = "문제집1";
        String description = "이 문제는 영어단어 모음입니다.";
        WorkbookTheme theme = WorkbookTheme.PINK;

        given(workbookService.createWorkbook(any()))
                .willReturn("workbook-id");

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

        result.andExpect(status().isOk())
                .andDo(document("question/workbook/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("문제집 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("문제집 설명"),
                                fieldWithPath("theme").type(JsonFieldType.STRING).optional().description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.WORKBOOK_THEME))
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("workbookId").type(JsonFieldType.STRING).description("문제집 id")
                        )
                ));
    }

    @Test
    @DisplayName("문제집의 정보를 수정할 수 있다.")
    void testUpdateWorkbookInfo() throws Exception {
        String workbookId = "workbook-id";
        String newName = "new 문제집";
        String newDescription = "new 문제집 설명글";

        ResultActions result = this.mockMvc.perform(
                put(UPDATE_WORKBOOK, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookUpdateDto(
                                newName, newDescription
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/workbook/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("변경하고싶은 문제집 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("변경하고싶은 문제집 설명")
                        )
                ));
    }

    @Test
    @DisplayName("문제집의 정보를 조회할 수 있다.")
    void testReadWorkbookInfo() throws Exception {
        String workbookId = "workbook-id";

        given(workbookService.readWorkbookInfo(anyString(), anyString()))
                .willReturn(new WorkbookVo(
                        workbookId,
                        "user-id",
                        "영단어 모음 - 상",
                        "알찬 구성으로 영단어를 모아봤습니다.",
                        4,
                        2,
                        WorkbookTheme.PINK
                ));

        ResultActions result = this.mockMvc.perform(
                get(READ_WORKBOOK_INFO, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/workbook/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("문제집 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("문제집 설명"),
                                fieldWithPath("problemCount").type(JsonFieldType.NUMBER).description("문항 개수"),
                                fieldWithPath("solvedCount").type(JsonFieldType.NUMBER).description("풀이 횟수")
                        )
                ));
    }
}