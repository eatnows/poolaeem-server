package com.poolaeem.poolaeem.user.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.TermsVersion;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.querydsl.core.annotations.QueryProjection;

public record UserVo(
    String id,
    String email,
    String name,
    UserRole role,
    OauthProvider oauthProvider,
    String oauthId,
    String profileImage,
    TermsVersion termsVersion,
    @JsonIgnore
    Boolean isDeleted
) {
    @QueryProjection
    public UserVo {
    }
}
