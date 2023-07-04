package com.poolaeem.poolaeem.workbook.infra.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.poolaeem.poolaeem.workbook.domain.entity.QWorkbook.workbook;

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
}
