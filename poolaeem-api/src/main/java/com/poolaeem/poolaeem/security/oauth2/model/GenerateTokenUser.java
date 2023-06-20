package com.poolaeem.poolaeem.security.oauth2.model;

import lombok.Getter;

@Getter
public class GenerateTokenUser {
    private String id;
    private String email;
    private String name;
    private String image;

    public GenerateTokenUser(String id, String email, String name, String image) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.image = image;
    }
}
