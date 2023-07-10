package com.poolaeem.poolaeem.question.domain.entity.vo;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProblemVo {
    private String id;
    private String workbookId;
    private String question;
    private List<ProblemOptionVo> options = new ArrayList<>();

    @QueryProjection
    public ProblemVo(String id, String workbookId, String question, List<ProblemOptionVo> options) {
        this.id = id;
        this.workbookId = workbookId;
        this.question = question;
        this.options = options;
    }
}
