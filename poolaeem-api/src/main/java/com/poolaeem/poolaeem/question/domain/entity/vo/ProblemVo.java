package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProblemVo {
    private String id;
    private String workbookId;
    private String question;
    private ProblemType type;
    private int optionCount;
    private List<ProblemOptionVo> options = new ArrayList<>();

    @QueryProjection
    public ProblemVo(String id, String workbookId, String question, ProblemType type, List<ProblemOptionVo> options) {
        this.id = id;
        this.workbookId = workbookId;
        this.question = question;
        this.type = type;
        this.optionCount = options.size();
        this.options = options;
    }
}
