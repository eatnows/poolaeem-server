package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.solve.application.GradingService;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.presentation.dto.GradingRequest;
import com.poolaeem.poolaeem.solve.presentation.dto.GradingResponse;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        String userId = Optional.ofNullable(user).map(UserVo::getId).orElse(null);

        List<Boolean> results = gradingService.gradeWorkbook(new SolveDto.WorkbookGradingParam(userId, workbookId, dto.name(), dto.problems()));
        GradingResponse.GradingResult response = new GradingResponse.GradingResult(dto.name(), results);
        return ApiResponseDto.OK(response);
    }
}
