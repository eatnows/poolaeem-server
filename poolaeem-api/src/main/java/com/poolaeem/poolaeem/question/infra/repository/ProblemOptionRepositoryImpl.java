package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.common.component.logged_user.LoggedInUser;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.QProblemOptionVo;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.poolaeem.poolaeem.question.domain.entity.QProblem.problem;
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

    @Override
    public List<ProblemOptionVo> findAllDtoByProblemIdInAndIsDeletedFalse(List<String> problemIds) {
        return queryFactory
                .select(new QProblemOptionVo(
                        problemOption.id,
                        problemOption.problem.id,
                        problemOption.value
                ))
                .from(problemOption)
                .where(problemOption.problem.id.in(problemIds), problemOption.isDeleted.isFalse())

                .fetch();
    }

    @Override
    public List<ProblemOptionVo> findAllCorrectAnswerByProblemIdIn(List<String> problemIds) {
        return queryFactory
                .select(new QProblemOptionVo(
                        problemOption.id,
                        problemOption.problem.id,
                        problemOption.value
                ))
                .from(problemOption)
                .where(problemOption.problem.id.in(problemIds), problemOption.isCorrect.isTrue(), problemOption.isDeleted.isFalse())
                .fetch();
    }

    @Override
    public List<ProblemOptionVo> findAllCorrectAnswerByWorkbook(Workbook workbook) {
        return queryFactory
                .select(new QProblemOptionVo(
                        problemOption.id,
                        problemOption.problem.id,
                        problemOption.value
                ))
                .from(problemOption)
                .innerJoin(problemOption.problem, problem)
                .on(problem.workbook.eq(workbook), problem.isDeleted.isFalse())
                .where(problemOption.isCorrect.isTrue(), problemOption.isDeleted.isFalse())
                .fetch();
    }
}
