package com.poolaeem.poolaeem.user.domain.service.auth;

import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.user.application.LoggedInHistoryRecord;
import com.poolaeem.poolaeem.user.domain.entity.LoggedInHistory;
import com.poolaeem.poolaeem.user.infra.repository.LoggedInHistoryRepository;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class LoggedInHistoryRecordImpl implements LoggedInHistoryRecord {
    private final UserRepository userRepository;
    private final LoggedInHistoryRepository loggedInHistoryRepository;

    public LoggedInHistoryRecordImpl(UserRepository userRepository, LoggedInHistoryRepository loggedInHistoryRepository) {
        this.userRepository = userRepository;
        this.loggedInHistoryRepository = loggedInHistoryRepository;
    }


    @Transactional
    @Override
    public void saveLoggedAt(String userId, HttpServletRequest request) {
        ZonedDateTime loggedAt = TimeComponent.nowUtc();

        userRepository.updateLastLoggedAtByIdAndIsDeletedFalse(userId, loggedAt);
        loggedInHistoryRepository.save(new LoggedInHistory(userId, loggedAt, getClientIp(request), getUserAgent(request)));
    }

    private String getUserAgent(HttpServletRequest request) {
        String agent = request.getHeader("user-agent");
        return agent == null ? "" : agent;
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("x-forwarded-for");

        return clientIp == null ? "" : clientIp;
    }
}
