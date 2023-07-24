package com.poolaeem.poolaeem.solve.infra.repository;

import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WorkbookResultRepositoryCustom {
    Slice<WorkbookResultVo> findAllDtoByWorkbookIdAndUserId(String workbookId, String lastResultId, Pageable pageable);
}
