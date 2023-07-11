package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface ProblemRepositoryCustom {
    Optional<Integer> findLastOrderByWorkbookAndIsDeleteFalse(Workbook workbook);

    Optional<Problem> findByIdAndIsDeletedFalseAndUserId(String problemId, String userId);

    Slice<ProblemVo> findAllByWorkbookIdAndUserIdAndIsDeletedFalse(String workbookId, String userId, int order, PageRequest pageable);
}
