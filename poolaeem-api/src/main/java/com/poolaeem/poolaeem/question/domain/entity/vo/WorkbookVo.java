package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class WorkbookVo {
    private String id;
    @JsonIgnore
    private String userId;
    private String name;
    private String description;
    private int problemCount;
    private int solvedCount;
    private WorkbookTheme theme;
    private ZonedDateTime createdAt;

    @QueryProjection
    public WorkbookVo(String id, String userId, String name, String description, int problemCount, int solvedCount, WorkbookTheme theme, ZonedDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.problemCount = problemCount;
        this.solvedCount = solvedCount;
        this.theme = theme;
        this.createdAt = createdAt;
    }
}
