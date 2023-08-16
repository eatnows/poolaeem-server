package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.vo.QWorkbookVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.poolaeem.poolaeem.question.domain.entity.QWorkbook.workbook;
import static com.querydsl.core.types.dsl.Expressions.asString;
import static com.querydsl.core.types.dsl.Expressions.nullExpression;

public class WorkbookRepositoryImpl implements WorkbookRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public WorkbookRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Integer> findLastOrderByUserIdAndIsDeletedFalse(String userId) {
        return Optional.ofNullable(queryFactory
                .select(workbook.order)
                .from(workbook)
                .where(workbook.userId.eq(userId), workbook.isDeleted.isFalse())
                .orderBy(workbook.order.desc())
                .fetchFirst());
    }

    @Override
    public Optional<WorkbookVo> findDtoByIdAndIsDeletedFalse(String workbookId) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QWorkbookVo(
                                asString(workbookId),
                                workbook.userId,
                                workbook.name,
                                workbook.description,
                                workbook.problemCount,
                                workbook.solvedCount,
                                workbook.theme,
                                workbook.createdAt
                        ))
                        .from(workbook)
                        .where(workbook.id.eq(workbookId), workbook.isDeleted.isFalse())
                        .fetchFirst());

    }

    @Override
    public void updateIncreaseProblemCountByWorkbookId(String workbookId) {
        queryFactory
                .update(workbook)
                .set(workbook.problemCount, workbook.problemCount.add(1))
                .where(workbook.id.eq(workbookId), workbook.isDeleted.isFalse())
                .execute();
    }

    @Override
    public void updateDecreaseProblemCountByWorkbookId(String workbookId) {
        queryFactory
                .update(workbook)
                .set(workbook.problemCount, workbook.problemCount.subtract(1))
                .where(workbook.id.eq(workbookId), workbook.isDeleted.isFalse())
                .execute();
    }

    @Override
    public void updateIncreaseSolvedCountByWorkbookId(String workbookId) {
        queryFactory
                .update(workbook)
                .set(workbook.solvedCount, workbook.solvedCount.add(1))
                .where(workbook.id.eq(workbookId), workbook.isDeleted.isFalse())
                .execute();
    }

    @Override
    public Slice<WorkbookVo> findAllUserIdAndDbStateFalseAndIdLessThan(String userId, Pageable pageable, String lastId) {
        List<WorkbookVo> content = queryFactory
                .select(new QWorkbookVo(
                        workbook.id,
                        nullExpression(),
                        workbook.name,
                        workbook.description,
                        workbook.problemCount,
                        workbook.solvedCount,
                        workbook.theme,
                        workbook.createdAt
                ))
                .from(workbook)
                .where(workbook.userId.eq(userId), workbook.isDeleted.isFalse(), ltId(lastId))
                .orderBy(workbook.id.desc())
                .limit(pageable.getPageSize() + 1L)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression ltId(String id) {
        return id == null ? null : workbook.id.lt(id);
    }
}
