package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.solve.application.SolveService;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;
import com.poolaeem.poolaeem.solve.presentation.dto.SolveResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SolveController {
    private final SolveService solveService;

    public SolveController(SolveService solveService) {
        this.solveService = solveService;
    }

    @GetMapping("/api/workbooks/{workbookId}/solve/introduction")
    public ApiResponseDto<SolveResponse.SolveInfoRead> readWorkbookSolveIntroduction(@PathVariable String workbookId) {
        WorkbookSolveDto.SolveInfoRead info = solveService.readSolveIntroduction(workbookId);
        SolveResponse.SolveInfoRead response = new SolveResponse.SolveInfoRead(info);
        return ApiResponseDto.OK(response);
    }
}
