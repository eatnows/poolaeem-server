package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.Problem;
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
                        problem.optionCount,
                        problem.order
                ))
                .from(problem)
                .where(problem.workbook.id.eq(workbookId), problem.isDeleted.isFalse(),
                        problem.order.lt(order))
                .orderBy(problem.order.desc())
                .limit(pageable.getPageSize() + 1L)
                .fetch();

        boolean hasNext = false;
        if (list.size() > pageable.getPageSize()) {
            hasNext = true;
            list.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(list, pageable, hasNext);
    }
}
