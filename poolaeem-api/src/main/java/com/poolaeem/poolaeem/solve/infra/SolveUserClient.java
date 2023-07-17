package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookCreator;

public interface SolveUserClient {
    WorkbookCreator readWorkbookCreator(String userId);
}
