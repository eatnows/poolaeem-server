package com.poolaeem.poolaeem.user.domain.service.auth;

import com.poolaeem.poolaeem.user.application.LoggedInHistoryRecord;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.TermsVersion;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.infra.repository.LoggedInHistoryRepository;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import com.poolaeem.poolaeem.user.infra.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 로그인 기록 테스트")
class LoggedInHistoryRecordImplTest {
    @InjectMocks
    private LoggedInHistoryRecordImpl loggedInHistoryRecord;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LoggedInHistoryRepository loggedInHistoryRepository;

    @Test
    @DisplayName("로그인한 시간을 기록하고, 마지막 로그인 시간을 변경한다")
    void saveLoggedInHistory() {
        String userId = "user-id";
        MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext("https://poolaeem.com"));

        loggedInHistoryRecord.saveLoggedAt(userId, request);

        verify(userRepository, times(1)).updateLastLoggedAtByIdAndIsDeletedFalse(anyString(), any());
        verify(loggedInHistoryRepository, times(1)).save(any());
    }
}