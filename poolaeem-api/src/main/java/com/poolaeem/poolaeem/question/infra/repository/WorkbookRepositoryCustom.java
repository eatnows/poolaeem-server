package com.poolaeem.poolaeem.question.infra.repository;

import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface WorkbookRepositoryCustom {
    Optional<Integer> findLastOrderByUserIdAndIsDeletedFalse(String userId);

    Optional<WorkbookVo> findDtoByIdAndIsDeletedFalse(String workbookId);

    void updateIncreaseProblemCountByWorkbookId(String workbookId);

    void updateDecreaseProblemCountByWorkbookId(String workbookId);

    void updateIncreaseSolvedCountByWorkbookId(String workbookId);

    Slice<WorkbookVo> findAllUserIdAndDbStateFalseAndIdLessThan(String userId, Pageable pageable, String lastId);
}
