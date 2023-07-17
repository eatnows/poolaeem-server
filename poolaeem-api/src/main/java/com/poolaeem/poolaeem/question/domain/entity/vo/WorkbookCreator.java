package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class WorkbookCreator {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userId;
    private String name;
    private String profileImageUrl;

    public WorkbookCreator(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public WorkbookCreator(String userId, String name, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
