package com.poolaeem.poolaeem.workbook.infra.repository;

import com.poolaeem.poolaeem.workbook.domain.entity.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookRepository extends JpaRepository<Workbook, String>, WorkbookRepositoryCustom {

}
