package com.poolaeem.poolaeem.solve.application;


import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;

import java.util.List;

public interface GradingService {
    List<Boolean> gradeWorkbook(SolveDto.WorkbookGradingParam param);
}
