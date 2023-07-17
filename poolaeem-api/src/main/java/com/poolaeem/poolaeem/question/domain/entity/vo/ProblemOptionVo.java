package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;

public class ProblemOptionVo {
    private String optionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String problemId;
    private String value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isCorrect;

    @QueryProjection
    public ProblemOptionVo(String id, String value, boolean isCorrect) {
        this.optionId = id;
        this.value = value;
        this.isCorrect = isCorrect;
    }

    @QueryProjection
    public ProblemOptionVo(String optionId, String problemId, String value) {
        this.optionId = optionId;
        this.problemId = problemId;
        this.value = value;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getProblemId() {
        return problemId;
    }

    public String getValue() {
        return value;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void removeProblemId() {
        this.problemId = null;
    }
}
