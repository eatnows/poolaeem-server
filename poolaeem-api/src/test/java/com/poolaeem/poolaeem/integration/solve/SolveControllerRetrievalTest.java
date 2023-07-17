package com.poolaeem.poolaeem.integration.solve;

import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

@DisplayName("통합: 풀이 관련 통합 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SolveControllerRetrievalTest extends BaseIntegrationTest {
    @Autowired
    private DataSource dataSource;

    @BeforeAll
    void init() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/workbook.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}