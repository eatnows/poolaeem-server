package com.poolaeem.poolaeem.solve.application;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;

public interface SolveService {
    WorkbookSolveDto.SolveInfoRead readSolveIntroduction(String workbookId);
}
