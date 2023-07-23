package com.poolaeem.poolaeem.config.jpa;

import com.poolaeem.poolaeem.common.file.FilePath;
import com.poolaeem.poolaeem.file.domain.entity.File;
import com.poolaeem.poolaeem.file.infra.repository.FileRepository;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.security.jwt.token.CustomUserDetail;
import com.poolaeem.poolaeem.security.jwt.token.PostAuthenticationToken;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.TermsVersion;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("통합: DateTimeProvider 테스트")
class DateTimeAuditingProviderTest extends BaseIntegrationTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    @DisplayName("엔티티를 새로 저장하면 createdAt, updatedAt이 자동적으로 값이 입력된다")
    void testDateTimeAuditing() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "file_name.txt", "text/plain", "<< file content >>".getBytes(StandardCharsets.UTF_8));
        SecurityContextHolder.getContext().setAuthentication(
                new PostAuthenticationToken(new CustomUserDetail(new UserVo(
                        "user-id",
                        "test@poolaeem.com",
                        "테스트",
                        UserRole.ROLE_USER,
                        OauthProvider.GOOGLE,
                        "1234",
                        null,
                        TermsVersion.V1
                )))
        );
        File entity = new File(
                "file-id",
                FilePath.PROFILE_IMAGE,
                mockFile
        );

        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        File entity2 = fileRepository.save(entity);
        assertThat(entity2.getCreatedAt()).isNotNull();
        assertThat(entity2.getUpdatedAt()).isNotNull();
    }
}