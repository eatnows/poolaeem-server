package com.poolaeem.poolaeem.user.infra.repository;

import com.poolaeem.poolaeem.user.domain.entity.LoggedInUserJwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoggedInUserJwtRepository extends JpaRepository<LoggedInUserJwt, String> {
    List<LoggedInUserJwt> findAllByUserId(String userId);

    Optional<LoggedInUserJwt> findByTokenAndClientIpAndUserAgent(String refreshToken, String clientIp, String userAgent);
}
