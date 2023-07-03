package com.poolaeem.poolaeem.integration.user.profile;

import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 유저 프로필 정보 조회 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileInfoControllerRetrievalTest extends BaseIntegrationTest {

    private final String READ_PROFILE_INFO = "/api/profile/info";

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/user/user.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("내 정보를 조회할 수 있다.")
    void testUserProfileInfoRetrieval() throws Exception {
        ResultActions result = this.mockMvc.perform(
                get(READ_PROFILE_INFO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.userId", is("user-1")))
                .andExpect(jsonPath("$.data.email", is("test@poolaeem.com")))
                .andExpect(jsonPath("$.data.name", is("풀내임")))
                .andExpect(jsonPath("$.data.profileImageUrl", nullValue()));
    }
}