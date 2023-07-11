package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemResponse {

    @Getter
    public static class ProblemRead {
        private String problemId;
        private String question;
        private ProblemType type;
        private List<ProblemOptionVo> options;

        public ProblemRead(ProblemVo problemVo) {
            this.problemId = problemVo.getId();
            this.question = problemVo.getQuestion();
            this.type = problemVo.getType();
            this.options = problemVo.getOptions();
        }
    }
}
