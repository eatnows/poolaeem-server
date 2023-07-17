package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Optional;

public interface ProblemRepositoryCustom {
    Optional<Integer> findLastOrderByWorkbookAndIsDeleteFalse(Workbook workbook);

    Optional<Problem> findByIdAndIsDeletedFalseAndUserId(String problemId, String userId);

    Slice<ProblemVo> findAllByWorkbookIdAndIsDeletedFalse(String workbookId, int order, Pageable pageable);

    void softDelete(Problem problem);

    SliceImpl<ProblemVo> findAllDtoByWorkbookIdAndIsDeletedFalse(String workbookId, int order, Pageable pageable);
}
