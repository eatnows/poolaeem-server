package com.poolaeem.poolaeem.question.application;

import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;

public interface ProblemService {
    void createProblem(ProblemDto.ProblemCreateParam param);

    ProblemVo readProblem(String userId, String problemId);

    void updateProblem(ProblemDto.ProblemUpdateParam param);
}
