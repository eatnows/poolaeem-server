package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkbookRepository extends JpaRepository<Workbook, String>, WorkbookRepositoryCustom {

    Optional<Workbook> findByIdAndIsDeletedFalse(String workbookId);
}
