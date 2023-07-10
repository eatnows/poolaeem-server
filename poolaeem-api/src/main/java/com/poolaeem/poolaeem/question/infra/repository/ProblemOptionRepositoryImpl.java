package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.poolaeem.poolaeem.question.domain.entity.QProblemOption.problemOption;

public class ProblemOptionRepositoryImpl implements ProblemOptionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ProblemOptionRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<ProblemOption> findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(String problemId) {
        return queryFactory
                .select(problemOption)
                .from(problemOption)
                .where(problemOption.problem.id.eq(problemId), problemOption.isDeleted.isFalse())
                .fetch();
    }
}
