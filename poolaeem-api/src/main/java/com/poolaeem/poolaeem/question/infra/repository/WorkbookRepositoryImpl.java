package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.QWorkbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.QWorkbookVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.asString;

public class WorkbookRepositoryImpl implements WorkbookRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public WorkbookRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Integer> findLastOrderByUserIdAndIsDeletedFalse(String userId) {
        return Optional.ofNullable(queryFactory
                .select(QWorkbook.workbook.order)
                .from(QWorkbook.workbook)
                .where(QWorkbook.workbook.userId.eq(userId), QWorkbook.workbook.isDeleted.isFalse())
                .orderBy(QWorkbook.workbook.order.desc())
                .fetchFirst());
    }

    @Override
    public Optional<WorkbookVo> findDtoByIdAndUserIdAndIsDeletedFalse(String workbookId) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QWorkbookVo(
                                asString(workbookId),
                                QWorkbook.workbook.userId,
                                QWorkbook.workbook.name,
                                QWorkbook.workbook.description,
                                QWorkbook.workbook.problemCount,
                                QWorkbook.workbook.solvedCount,
                                QWorkbook.workbook.theme
                        ))
                        .from(QWorkbook.workbook)
                        .where(QWorkbook.workbook.id.eq(workbookId), QWorkbook.workbook.isDeleted.isFalse())
                        .fetchFirst());

    }
}
