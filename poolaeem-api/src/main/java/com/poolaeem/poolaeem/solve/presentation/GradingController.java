package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.solve.application.GradingService;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.presentation.dto.GradingRequest;
import com.poolaeem.poolaeem.solve.presentation.dto.GradingResponse;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GradingController {
    private final GradingService gradingService;

    public GradingController(GradingService gradingService) {
        this.gradingService = gradingService;
    }

    @PostMapping("/api/workbooks/{workbookId}/grade")
    public ApiResponseDto<GradingResponse.GradingResult> gradeWorkbook(@LoggedInUser UserVo user,
                                                                       @PathVariable String workbookId,
                                                                       @Valid @RequestBody GradingRequest.WorkbookGrade dto) {
        String userId = null;
        if (user != null) {
            userId = user.getId();
        }

        List<Boolean> results = gradingService.gradeWorkbook(new SolveDto.WorkbookGradingParam(userId, workbookId, dto.getName(), dto.getProblems()));
        GradingResponse.GradingResult response = new GradingResponse.GradingResult(dto.getName(), results);
        return ApiResponseDto.OK(response);
    }
}
