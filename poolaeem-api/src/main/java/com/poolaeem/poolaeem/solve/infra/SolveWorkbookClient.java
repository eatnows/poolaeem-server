package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;

public interface SolveWorkbookClient {

    WorkbookSolveDto.SolveInfoRead readWorkbookInfo(String workbookId);
}
