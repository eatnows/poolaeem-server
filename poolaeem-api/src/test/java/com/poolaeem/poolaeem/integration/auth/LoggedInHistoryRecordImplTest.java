package com.poolaeem.poolaeem.integration.auth;

import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.user.application.LoggedInHistoryRecord;
import com.poolaeem.poolaeem.user.domain.entity.LoggedInHistory;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.infra.repository.LoggedInHistoryRepository;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("통합: 로그인 기록 테스트")
class LoggedInHistoryRecordImplTest extends BaseIntegrationTest {
    @Autowired
    private LoggedInHistoryRecord loggedInHistoryRecord;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoggedInHistoryRepository loggedInHistoryRepository;

    @Test
    @DisplayName("로그인 내역을 수집하고 마지막 로그인 기록을 변경할 수 있다")
    void testSaveLoggedInHistory() {
        String userId = "user-1";
        MockHttpServletRequest request = new MockHttpServletRequest(new MockServletContext("https://poolaeem.com"));

        User before = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(before.getLastLoggedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).isEqualTo("2023-06-25T00:52:17.123");
        List<LoggedInHistory> beforeHistories = loggedInHistoryRepository.findAll();
        assertThat(beforeHistories).isEmpty();

        loggedInHistoryRecord.saveLoggedAt(userId, request);

        User after = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(after.getLastLoggedAt().format(DateTimeFormatter.ISO_LOCAL_DATE)).isNotEqualTo("2023-06-25T00:52:17.123");
        List<LoggedInHistory> afterHistories = loggedInHistoryRepository.findAll();
        assertThat(afterHistories).hasSize(1);
    }
}