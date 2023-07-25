package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;

import java.util.List;

public interface ProblemOptionRepositoryCustom {
    List<ProblemOption> findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(String problemId);

    void softDeleteAllByProblem(Problem problem);

    void softDeleteAllByIdIn(List<ProblemOption> deleteList);

    List<ProblemOptionVo> findAllDtoByProblemIdInAndIsDeletedFalse(List<String> problemIds);

    List<ProblemOptionVo> findAllCorrectAnswerByProblemIdIn(List<String> problemIds);

    List<ProblemOptionVo> findAllCorrectAnswerByWorkbook(Workbook workbook);
    void softDeleteAllByWorkbook(Workbook workbook);

}
