package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Workbook;

import java.util.Optional;

public interface ProblemRepositoryCustom {
    Optional<Integer> findLastOrderByWorkbookAndIsDeleteFalse(Workbook workbook);
}
