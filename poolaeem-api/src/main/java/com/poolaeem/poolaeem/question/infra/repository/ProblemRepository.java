package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, String>, ProblemRepositoryCustom {
    Optional<Problem> findByIdAndIsDeletedFalse(String problemId);
}
