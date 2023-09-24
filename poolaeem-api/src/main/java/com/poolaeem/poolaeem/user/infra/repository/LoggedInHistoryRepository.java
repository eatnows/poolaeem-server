package com.poolaeem.poolaeem.user.infra.repository;

import com.poolaeem.poolaeem.user.domain.entity.LoggedInHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggedInHistoryRepository extends JpaRepository<LoggedInHistory, String> {
}
