package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.common.exception.common.EntityNotFoundException;
import com.poolaeem.poolaeem.question.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookCreator;
import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentFormatGenerator;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentLinkGenerator;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.question.presentation.dto.WorkbookRequest;
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
    private final String READ_WORKBOOK_SOLVE_INTRODUCTION = "/api/workbooks/{workbookId}/solve/introduction";


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
                        WorkbookTheme.PINK,
                        ZonedDateTime.now()));

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
                                fieldWithPath("solvedCount").type(JsonFieldType.NUMBER).description("풀이 횟수"),
                                fieldWithPath("theme").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.WORKBOOK_THEME))
                        )
                ));
    }

    @Test
    @DisplayName("문제집 풀이 정보를 조회할 수 있다.")
    void testReadWorkbookSolveIntroduction() throws Exception {
        String workbookId = "workbook-id";

        given(workbookService.readSolveIntroduction(workbookId))
                .willReturn(new WorkbookDto.SolveIntroductionRead(
                        workbookId,
                        "문제집1",
                        "기초 영단어 문제 모음",
                        WorkbookTheme.PINK,
                        new WorkbookCreator(
                                "원작자",
                                "https://image.poolaeem.com/test/profile/1234"
                        ),
                        ZonedDateTime.of(LocalDateTime.of(2023, 7, 16, 16, 16, 30), ZoneId.of(ZoneOffset.UTC.getId())),
                        30,
                        12
                ));

        ResultActions result = this.mockMvc.perform(
                get(READ_WORKBOOK_SOLVE_INTRODUCTION, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/workbook/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("문제집 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("문제집 설명글"),
                                fieldWithPath("theme").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.WORKBOOK_THEME)),
                                fieldWithPath("creator.name").type(JsonFieldType.STRING).description("문제집 만든이"),
                                fieldWithPath("creator.profileImageUrl").type(JsonFieldType.STRING).description("만든이 프로필 이미지 주소"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateTimeFormat()).description("문제집 생성일"),
                                fieldWithPath("problemCount").type(JsonFieldType.NUMBER).description("문제집의 문항수"),
                                fieldWithPath("solvedCount").type(JsonFieldType.NUMBER).description("문제집을 풀이한 수")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 문제집 소개는 조회할 수 없다.")
    void testReadWorkbookSolveIntroductionForNotFound() throws Exception {
        String workbookId = "not-exist-workbook";

        given(workbookService.readSolveIntroduction(workbookId))
                .willThrow(new EntityNotFoundException());

        ResultActions result = this.mockMvc.perform(
                get(READ_WORKBOOK_SOLVE_INTRODUCTION, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound())
                .andDo(document("question/workbook/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}