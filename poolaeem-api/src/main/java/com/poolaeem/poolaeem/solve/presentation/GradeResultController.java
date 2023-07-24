package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.solve.application.GradeResultService;
import com.poolaeem.poolaeem.solve.domain.dto.GradeResultDto;
import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import com.poolaeem.poolaeem.solve.presentation.dto.GradeResultResponse;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GradeResultController {
    private final GradeResultService gradeResultService;

    public GradeResultController(GradeResultService gradeResultService) {
        this.gradeResultService = gradeResultService;
    }

    @GetMapping("/api/results/workbooks/{workbookId}/solved/history")
    public ApiResponseDto<GradeResultResponse.SolvedUsersRead> readSolvedHistoryOfWorkbook(@LoggedInUser UserVo user,
                                                                                           @PathVariable String workbookId,
                                                                                           @RequestParam(required = false) String lastId,
                                                                                           @RequestParam(defaultValue = "30") @Max(100) int size) {
        Slice<WorkbookResultVo> results = gradeResultService.readSolvedHistoryOfWorkbook(new GradeResultDto.SolvedUsersReadParam(user.getId(), workbookId, lastId, size));

        GradeResultResponse.SolvedUsersRead response = new GradeResultResponse.SolvedUsersRead(results);
        return ApiResponseDto.OK(response);
    }
}
