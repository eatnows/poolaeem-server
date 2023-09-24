package com.poolaeem.poolaeem.user.application;

import jakarta.servlet.http.HttpServletRequest;

public interface LoggedInHistoryRecord {
    void saveLoggedAt(String userId, HttpServletRequest request);
}
