package com.poolaeem.poolaeem.user.domain.entity;

import lombok.Getter;

@Getter
public enum OauthProvider {

    GOOGLE("google");

    private String id;

    OauthProvider(String id) {
        this.id = id;
    }
}
