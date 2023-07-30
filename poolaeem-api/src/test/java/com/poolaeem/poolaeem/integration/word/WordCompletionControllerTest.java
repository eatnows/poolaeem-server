package com.poolaeem.poolaeem.integration.word;

import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.word.domain.WordLang;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("통합: 단어 완성 목록 조회 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WordCompletionControllerTest extends BaseIntegrationTest {
    private final String COMPLETE_WORD = "/api/word/{word}/complete";

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    void init() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/word/english_word.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/word/korean_word.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("word 문자열에 한글이 포함되어 있으면 한국어 단어 완성 목록을 조회한다.")
    void testCompleteKoreanWord() throws Exception {
        String word = "사과";

        mockMvc.perform(
                        get(COMPLETE_WORD, word)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.words[0]", is("사과")))
                .andExpect(jsonPath("$.data.words[1]", is("사과하다")))
                .andExpect(jsonPath("$.data.words[2]", is("사과밭")))
                .andExpect(jsonPath("$.data.words[3]", is("사과독나방")))
                .andExpect(jsonPath("$.data.words[4]", is("사과탕")));
    }

    @Test
    @DisplayName("word 문자열에 한글이 포함되어 있지않으면 영단어 완성 목록을 조회한다.")
    void testCompleteEnglishWord() throws Exception {
        String word = "appl";

        mockMvc.perform(
                        get(COMPLETE_WORD, word)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.words[0]", is("appl")))
                .andExpect(jsonPath("$.data.words[1]", is("application")))
                .andExpect(jsonPath("$.data.words[2]", is("apply")))
                .andExpect(jsonPath("$.data.words[3]", is("apple")))
                .andExpect(jsonPath("$.data.words[4]", is("applicable")));
    }

    @Test
    @DisplayName("lang 파라미터가 존재하면 word 문자열과 관계없이 해당 언어의 단어를 검색한다.")
    void testCompleteWordForExistLangParameter() throws Exception {
        String word = "appl";

        mockMvc.perform(
                        get(COMPLETE_WORD, word)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("lang", WordLang.KOREAN.name())
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                // 한국어 단어 목록엔 appl로 시작하는 영단어가 존재하지 않음
                .andExpect(jsonPath("$.data.words", hasSize(0)));
    }
}