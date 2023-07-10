package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.poolaeem.poolaeem.question.domain.entity.QProblem.problem;
import static com.poolaeem.poolaeem.question.domain.entity.QWorkbook.workbook;

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

    @Override
    public Optional<Problem> findByIdAndIsDeletedFalseAndUserId(String problemId, String userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(problem)
                        .innerJoin(problem.workbook, workbook)
                        .on(workbook.isDeleted.isFalse(), workbook.userId.eq(userId))
                        .where(problem.id.eq(problemId), problem.isDeleted.isFalse())
                        .fetchFirst()
        );
    }
}
