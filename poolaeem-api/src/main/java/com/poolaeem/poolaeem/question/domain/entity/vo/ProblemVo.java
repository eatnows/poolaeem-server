package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class ProblemVo {
    private String problemId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workbookId;
    private String question;
    private ProblemType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer timeout;
    private int optionCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProblemOptionVo> options;

    public ProblemVo(String problemId, String workbookId, String question, ProblemType type, Integer timeout, List<ProblemOptionVo> options) {
        this.problemId = problemId;
        this.workbookId = workbookId;
        this.question = question;
        this.type = type;
        this.timeout = timeout;
        this.optionCount = options.size();
        this.options = options;
    }

    @QueryProjection
    public ProblemVo(String problemId, String question, ProblemType type, int optionCount) {
        this.problemId = problemId;
        this.question = question;
        this.type = type;
        this.optionCount = optionCount;
    }
}
