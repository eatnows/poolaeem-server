package com.poolaeem.poolaeem.solve.application;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;

public interface SolveService {
    WorkbookSolveDto.SolveInfoRead readSolveInfo(String workbookId);
}
