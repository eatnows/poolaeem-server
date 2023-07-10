package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;

import java.util.Optional;

public interface WorkbookRepositoryCustom {
    Optional<Integer> findLastOrderByUserIdAndIsDeletedFalse(String userId);

    Optional<WorkbookVo> findDtoByIdAndUserIdAndIsDeletedFalse(String workbookId);

    void updateAddProblemCountByWorkbookId(String workbookId);
}
