package com.poolaeem.poolaeem.user.infra.repository;

import com.poolaeem.poolaeem.user.domain.entity.LoggedInUserJwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoggedInUserJwtRepository extends JpaRepository<LoggedInUserJwt, String> {
    List<LoggedInUserJwt> findAllByUserId(String userId);
}
