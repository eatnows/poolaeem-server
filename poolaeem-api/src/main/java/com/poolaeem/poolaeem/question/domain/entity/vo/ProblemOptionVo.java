package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;

public class ProblemOptionVo {
    private String optionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String problemId;
    private String value;
    private boolean isCorrect;

    @QueryProjection
    public ProblemOptionVo(String id, String value, boolean isCorrect) {
        this.optionId = id;
        this.value = value;
        this.isCorrect = isCorrect;
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

    public boolean getIsCorrect() {
        return isCorrect;
    }
}
