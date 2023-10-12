package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.querydsl.core.annotations.QueryProjection;

import java.time.ZonedDateTime;

public record WorkbookVo(
    String id,
    @JsonIgnore
    String userId,
    String name,
    String description,
    int problemCount,
    int solvedCount,
    WorkbookTheme theme,
    ZonedDateTime createdAt
) {
    @QueryProjection
    public WorkbookVo {
    }
}
