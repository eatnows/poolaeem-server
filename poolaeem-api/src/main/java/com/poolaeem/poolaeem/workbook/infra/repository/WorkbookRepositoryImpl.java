package com.poolaeem.poolaeem.workbook.infra.repository;

import com.poolaeem.poolaeem.workbook.domain.entity.vo.QWorkbookVo;
import com.poolaeem.poolaeem.workbook.domain.entity.vo.WorkbookVo;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.poolaeem.poolaeem.workbook.domain.entity.QWorkbook.workbook;
import static com.querydsl.core.types.dsl.Expressions.asString;

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
    public Optional<WorkbookVo> findDtoByIdAndUserIdAndIsDeletedFalse(String workbookId) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QWorkbookVo(
                                asString(workbookId),
                                workbook.userId,
                                workbook.name,
                                workbook.description,
                                workbook.problemCount,
                                workbook.solvedCount,
                                workbook.theme
                        ))
                        .from(workbook)
                        .where(workbook.id.eq(workbookId), workbook.isDeleted.isFalse())
                        .fetchFirst());

    }
}
