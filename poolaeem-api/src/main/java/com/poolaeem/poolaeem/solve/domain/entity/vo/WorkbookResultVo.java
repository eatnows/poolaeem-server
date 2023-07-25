package com.poolaeem.poolaeem.solve.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class WorkbookResultVo {
    private String resultId;
    @JsonIgnore
    private String workbookId;
    private String userId;
    private String userName;
    private Integer totalQuestions;
    private Integer correctCount;
    @JsonFormat(pattern = TimeComponent.DATETIME_PATTERN, timezone = TimeComponent.DEFAULT_TIMEZONE)
    private ZonedDateTime solvedAt;

    @QueryProjection
    public WorkbookResultVo(String resultId, String userName, Integer totalQuestions, Integer correctCount, ZonedDateTime solvedAt) {
        this.resultId = resultId;
        this.userName = userName;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.solvedAt = solvedAt;
    }
}
