package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.solve.application.SolveService;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.presentation.dto.SolveRequest;
import com.poolaeem.poolaeem.solve.presentation.dto.SolveResponse;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SolveController {
    private final SolveService solveService;

    public SolveController(SolveService solveService) {
        this.solveService = solveService;
    }

    @PostMapping("/api/workbooks/{workbookId}/solve")
    public ApiResponseDto<SolveResponse.SolveResult> solveWorkbook(@LoggedInUser UserVo user,
                                           @PathVariable String workbookId,
                                           @Valid @RequestBody SolveRequest.WorkbookSolve dto) {
        String userId = null;
        if (user != null) {
            userId = user.getId();
        }

        List<Boolean> results = solveService.solveWorkbook(new SolveDto.WorkbookSolveParam(userId, workbookId, dto.getName(), dto.getProblems()));
        SolveResponse.SolveResult response = new SolveResponse.SolveResult(dto.getName(), results);
        return ApiResponseDto.OK(response);
    }
}
