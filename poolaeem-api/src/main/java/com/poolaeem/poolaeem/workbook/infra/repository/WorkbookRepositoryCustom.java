package com.poolaeem.poolaeem.workbook.infra.repository;

import java.util.Optional;

public interface WorkbookRepositoryCustom {
    Optional<Integer> findLastOrderByUserIdAndIsDeletedFalse(String userId);
}
