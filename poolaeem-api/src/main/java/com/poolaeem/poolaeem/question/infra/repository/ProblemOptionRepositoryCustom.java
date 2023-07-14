package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;

import java.util.List;

public interface ProblemOptionRepositoryCustom {
    List<ProblemOption> findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(String problemId);

    void softDeleteAllByProblem(Problem problem);

    void softDeleteAllByIdIn(List<ProblemOption> deleteList);
}
