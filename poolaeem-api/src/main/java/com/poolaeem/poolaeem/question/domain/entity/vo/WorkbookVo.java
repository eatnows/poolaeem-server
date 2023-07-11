package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

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
    private int order;

    @QueryProjection
    public WorkbookVo(String id, String userId, String name, String description, int problemCount, int solvedCount, WorkbookTheme theme) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.problemCount = problemCount;
        this.solvedCount = solvedCount;
        this.theme = theme;
    }
}
