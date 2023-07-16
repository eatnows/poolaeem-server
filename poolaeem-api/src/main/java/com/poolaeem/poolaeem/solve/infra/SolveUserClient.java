package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookAuthor;

public interface SolveUserClient {
    WorkbookAuthor readWorkbookAuthor(String userId);
}
