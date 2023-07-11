package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;

import java.util.Optional;

public interface WorkbookRepositoryCustom {
    Optional<Integer> findLastOrderByUserIdAndIsDeletedFalse(String userId);

    Optional<WorkbookVo> findDtoByIdAndIsDeletedFalse(String workbookId);

    void updateIncreaseProblemCountByWorkbookId(String workbookId);

    void updateDecreaseProblemCountByWorkbookId(String workbookId);
}
