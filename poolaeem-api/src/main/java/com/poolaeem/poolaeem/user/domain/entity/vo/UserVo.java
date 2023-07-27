package com.poolaeem.poolaeem.user.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.TermsVersion;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserVo {
    private String id;
    private String email;
    private String name;
    private UserRole role;
    private OauthProvider oauthProvider;
    private String oauthId;
    private String profileImage;
    private TermsVersion termsVersion;
    @JsonIgnore
    private Boolean isDeleted;

    @QueryProjection
    public UserVo(String id, String email, String name, UserRole role, OauthProvider oauthProvider, String oauthId, String profileImage, TermsVersion termsVersion, Boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
        this.profileImage = profileImage;
        this.termsVersion = termsVersion;
        this.isDeleted = isDeleted;
    }
}
