package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemResponse {

    public record ProblemRead(
            String problemId,
            String question,
            ProblemType type,
            int timeout,
            List<ProblemOptionVo> options
    ) {
        public ProblemRead(ProblemVo problemVo) {
            this(problemVo.getProblemId(), problemVo.getQuestion(), problemVo.getType(), problemVo.getTimeout(), problemVo.getOptions());
        }
    }

    public record ProblemListRead(
            List<ProblemVo> problems,
            boolean hasNext
    ) {
        public ProblemListRead(Slice<ProblemVo> problems) {
            this(problems.getContent(), problems.hasNext());
        }
    }

    public record SolveProblemsRead(
            List<ProblemVo> problems,
            boolean hasNext
    ) {
        public SolveProblemsRead(Slice<ProblemVo> problems) {
            this(problems.getContent(), problems.hasNext());
        }
    }
}
