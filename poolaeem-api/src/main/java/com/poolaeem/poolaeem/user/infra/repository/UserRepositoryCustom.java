package com.poolaeem.poolaeem.user.infra.repository;

import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<UserVo> findByUserIdAndIsDeletedFalse(String userId);
}
