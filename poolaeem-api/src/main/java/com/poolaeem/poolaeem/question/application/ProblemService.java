package com.poolaeem.poolaeem.question.application;

import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;

public interface ProblemService {
    void createProblem(ProblemDto.ProblemCreateParam param);
}
