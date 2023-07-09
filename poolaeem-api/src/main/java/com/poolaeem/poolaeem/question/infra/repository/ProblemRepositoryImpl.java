package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.poolaeem.poolaeem.question.domain.entity.QProblem.problem;

public class ProblemRepositoryImpl implements ProblemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ProblemRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Integer> findLastOrderByWorkbookAndIsDeleteFalse(Workbook workbook) {
        return Optional.ofNullable(
                queryFactory
                .select(problem.order)
                .from(problem)
                .where(problem.workbook.eq(workbook), problem.isDeleted.isFalse())
                .orderBy(problem.order.desc())
                .fetchFirst()
        );
    }
}
