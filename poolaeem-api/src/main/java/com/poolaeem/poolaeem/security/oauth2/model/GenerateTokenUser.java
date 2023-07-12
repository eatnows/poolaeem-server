package com.poolaeem.poolaeem.security.oauth2.model;

import lombok.Getter;

@Getter
public class GenerateTokenUser {
    private String id;

    public GenerateTokenUser(String id) {
        this.id = id;
    }
}
