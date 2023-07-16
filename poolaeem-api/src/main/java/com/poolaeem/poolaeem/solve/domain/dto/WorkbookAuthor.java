package com.poolaeem.poolaeem.solve.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class WorkbookAuthor {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userId;
    private String name;
    private String profileImageUrl;

    public WorkbookAuthor(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public WorkbookAuthor(String userId, String name, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
