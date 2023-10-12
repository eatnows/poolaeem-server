package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

public record WorkbookCreator(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String userId,
    String name,
    String profileImageUrl
) {
    public WorkbookCreator(String name, String profileImageUrl) {
        this(null, name, profileImageUrl);
    }
}
