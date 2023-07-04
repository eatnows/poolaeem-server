package com.poolaeem.poolaeem.integration.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolaeem.poolaeem.integration.config.LocalStackS3Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
@SqlGroup(value = {
        @Sql(scripts = {
                "classpath:/sql/user/oauth2.sql",
        }),
        @Sql(scripts = "classpath:/sql/user/user.sql")
})
public class BaseIntegrationTest {

    protected final String ACCESS_TOKEN = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6InVzZXItMSIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3OTUyMTE4LCJleHAiOjM2ODc5NTM5MTh9.QHwZ4Jppb5Bgp-oain43voHX5J4bkzt-7zTWKQOjFI77_H4k0HXYe79RjJopmvrOHX3YEFECfDJuTOgoMUqBQGFRmqSdEmUF-vFXOcjezvxw4NQzFTFkq430sNxrhK_RMsSbKTMnByZuN7Opj_RNGmGlDygX1dyqqqwxFZPn3fivl5TUM9VNhGACbCEGzxIYi7ACGNR2Kj61Qf343Dxyw37bXQ0D3a63Izxq8ThAzOtIyICnnF_ZYvn-3Y1nx9cpcxMdqwIGVFYBYSOOaCKnLJ7BUxEvy8l9tqwmGd2jCJWAAeSKNfcGmo4jpPrj5jJFvBoV2ynygZHHvkIXP0v_LQ";
    protected final String BEARER_ACCESS_TOKEN = "Bearer " + ACCESS_TOKEN;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;


}
