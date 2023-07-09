package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemOptionRepository extends JpaRepository<ProblemOption, String> {
}
