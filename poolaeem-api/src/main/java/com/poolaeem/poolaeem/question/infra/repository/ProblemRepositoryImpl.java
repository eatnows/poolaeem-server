package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.common.component.logged_user.LoggedInUser;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.QProblem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.QProblemVo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
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

    @Override
    public Slice<ProblemVo> findAllByWorkbookIdAndIsDeletedFalse(String workbookId, int order, Pageable pageable) {
        List<ProblemVo> list = queryFactory
                .select(new QProblemVo(
                        problem.id,
                        problem.question,
                        problem.type,
                        problem.optionCount
                ))
                .from(problem)
                .where(problem.workbook.id.eq(workbookId), problem.isDeleted.isFalse(),
                        problem.order.gt(order))
                .orderBy(problem.order.asc())
                .limit(pageable.getPageSize() + 1L)
                .fetch();

        boolean hasNext = false;
        if (list.size() > pageable.getPageSize()) {
            hasNext = true;
            list.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(list, pageable, hasNext);
    }

    @Override
    public void softDelete(Problem problem) {
        queryFactory
                .update(QProblem.problem)
                .set(QProblem.problem.isDeleted, true)
                .set(QProblem.problem.updatedBy, LoggedInUser.getUserId())
                .set(QProblem.problem.updatedAt, TimeComponent.nowUtc())
                .where(QProblem.problem.eq(problem), QProblem.problem.isDeleted.isFalse())
                .execute();
    }

    @Override
    public SliceImpl<ProblemVo> findAllDtoByWorkbookIdAndIsDeletedFalse(String workbookId, int order, Pageable pageable) {
        List<ProblemVo> result = queryFactory
                .select(new QProblemVo(
                        problem.id,
                        problem.question,
                        problem.type,
                        problem.timeout
                ))
                .from(problem)
                .where(problem.workbook.id.eq(workbookId), problem.isDeleted.isFalse(),
                        problem.order.gt(order))
                .orderBy(problem.order.asc())
                .limit(pageable.getPageSize() + 1L)
                .fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            hasNext = true;
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
