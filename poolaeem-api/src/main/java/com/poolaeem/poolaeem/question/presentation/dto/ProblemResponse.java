package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemResponse {

    @Getter
    public static class ProblemRead {
        private String problemId;
        private String question;
        private ProblemType type;
        private int timeout;
        private List<ProblemOptionVo> options;

        public ProblemRead(ProblemVo problemVo) {
            this.problemId = problemVo.getProblemId();
            this.question = problemVo.getQuestion();
            this.type = problemVo.getType();
            this.options = problemVo.getOptions();
            this.timeout = problemVo.getTimeout();
        }
    }

    @Getter
    public static class ProblemListRead {
        private List<ProblemVo> problems;
        private boolean hasNext;
        public ProblemListRead(Slice<ProblemVo> problems) {
            this.problems = problems.getContent();
            this.hasNext = problems.hasNext();
        }
    }
}
