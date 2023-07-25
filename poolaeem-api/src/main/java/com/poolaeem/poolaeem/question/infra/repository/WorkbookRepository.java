package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkbookRepository extends JpaRepository<Workbook, String>, WorkbookRepositoryCustom {

    Optional<Workbook> findByIdAndIsDeletedFalse(String workbookId);
    @Query("select w from Workbook w join fetch w.problems p where w.id = :workbookId and p.isDeleted = false")
    Optional<Workbook> findByIdAndIsDeletedFalseFetchJoinProblems(String workbookId);
}
