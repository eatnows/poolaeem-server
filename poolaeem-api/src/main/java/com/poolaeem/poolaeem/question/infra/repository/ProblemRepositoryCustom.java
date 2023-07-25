package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface ProblemRepositoryCustom {
    Optional<Integer> findLastOrderByWorkbookAndIsDeleteFalse(Workbook workbook);

    Optional<Problem> findByIdAndIsDeletedFalseAndUserId(String problemId, String userId);

    void softDelete(Problem problem);

    Slice<ProblemVo> findAllDtoByWorkbookIdAndIsDeletedFalse(String workbookId, int order, Pageable pageable);

    List<ProblemVo> findAllProblemIdAndTypeByWorkbook(Workbook workbook);

    void softDeleteAllByWorkbook(Workbook workbook);
}
