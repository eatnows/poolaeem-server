package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.common.component.logged_user.LoggedInUser;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
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
                .orderBy(problemOption.order.asc())
                .fetch();
    }

    @Override
    public void softDeleteAllByProblem(Problem problem) {
        queryFactory
                .update(problemOption)
                .set(problemOption.isDeleted, true)
                .set(problemOption.updatedBy, LoggedInUser.getUserId())
                .set(problemOption.updatedAt, TimeComponent.nowUtc())
                .where(problemOption.problem.eq(problem), problemOption.isDeleted.isFalse())
                .execute();
    }

    @Override
    public void softDeleteAllByIdIn(List<ProblemOption> deleteList) {
        queryFactory
                .update(problemOption)
                .set(problemOption.isDeleted, true)
                .set(problemOption.updatedBy, LoggedInUser.getUserId())
                .set(problemOption.updatedAt, TimeComponent.nowUtc())
                .where(problemOption.in(deleteList), problemOption.isDeleted.isFalse())
                .execute();
    }
}
