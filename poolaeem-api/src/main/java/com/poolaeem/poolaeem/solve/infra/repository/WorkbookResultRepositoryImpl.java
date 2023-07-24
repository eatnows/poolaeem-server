package com.poolaeem.poolaeem.solve.infra.repository;

import com.poolaeem.poolaeem.solve.domain.entity.vo.QWorkbookResultVo;
import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.poolaeem.poolaeem.solve.domain.entity.QWorkbookResult.workbookResult;

public class WorkbookResultRepositoryImpl implements WorkbookResultRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public WorkbookResultRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Slice<WorkbookResultVo> findAllDtoByWorkbookIdAndUserId(String workbookId, String lastResultId, Pageable pageable) {
        List<WorkbookResultVo> contents = queryFactory
                .select(new QWorkbookResultVo(
                        workbookResult.id,
                        workbookResult.userName,
                        workbookResult.totalQuestions,
                        workbookResult.correctCount,
                        workbookResult.createdAt
                ))
                .from(workbookResult)
                .where(workbookResult.workbookId.eq(workbookId),
                        ltResultId(lastResultId))
                .orderBy(workbookResult.id.desc())
                .limit(pageable.getPageSize() + 1L)
                .fetch();

        boolean hasNext = false;
        if (contents.size() > pageable.getPageSize()) {
            hasNext = true;
            contents.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private BooleanExpression ltResultId(String lastResultId) {
        return lastResultId == null ? null : workbookResult.id.lt(lastResultId);
    }
}
