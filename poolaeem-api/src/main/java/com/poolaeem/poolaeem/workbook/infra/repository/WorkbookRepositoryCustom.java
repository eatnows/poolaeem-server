package com.poolaeem.poolaeem.workbook.infra.repository;

import com.poolaeem.poolaeem.workbook.domain.entity.vo.WorkbookVo;

import java.util.Optional;

public interface WorkbookRepositoryCustom {
    Optional<Integer> findLastOrderByUserIdAndIsDeletedFalse(String userId);

    Optional<WorkbookVo> findDtoByIdAndUserIdAndIsDeletedFalse(String workbookId);
}
