package com.poolaeem.poolaeem.word.presentation;

import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentLinkGenerator;
import com.poolaeem.poolaeem.word.domain.WordLang;
import com.poolaeem.poolaeem.word.domain.service.EnglishWordCompletionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("단위: 단어 완성 컨트롤러 테스트")
class WordCompletionControllerTest extends ApiDocumentationTest {
    private final String COMPLETE_WORD = "/api/word/{word}/complete";

    @Test
    @DisplayName("단어 완성 목록을 조회할 수 있다.")
    void testCompleteWord() throws Exception {
        String word = "appl";
        WordLang wordLang = WordLang.ENGLISH;

        given(wordCompletionServiceStrategy.findService(word, wordLang))
                .willReturn(wordCompletionService);
        given(wordCompletionService.completeWord(anyString()))
                .willReturn(List.of("appl", "application", "apply", "apple", "applicable"));

        mockMvc.perform(
                        get(COMPLETE_WORD, word)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("lang", wordLang.name())
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(document("word/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("word").description("검색 키워드")
                        ),
                        queryParameters(
                                parameterWithName("lang").optional().description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.WORD_LANGUAGE))
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("words").type(JsonFieldType.ARRAY).description("단어 목록")
                        )
                ));
    }

}