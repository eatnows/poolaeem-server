package com.poolaeem.poolaeem.workbook.infra.repository;

import com.poolaeem.poolaeem.workbook.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, String> {
}
