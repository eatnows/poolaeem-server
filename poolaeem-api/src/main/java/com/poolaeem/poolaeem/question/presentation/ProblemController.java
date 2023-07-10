package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemRequest;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProblemController {
    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping("/api/workbooks/{workbookId}/problem")
    public ApiResponseDto<?> createProblem(@LoggedInUser UserVo user,
                                           @PathVariable String workbookId,
                                           @Valid @RequestBody ProblemRequest.ProblemCreate dto) {
        ProblemDto.ProblemCreateParam param =
                new ProblemDto.ProblemCreateParam(workbookId, user.getId(), dto.getQuestion(), dto.getOptions());
        problemService.createProblem(param);

        return ApiResponseDto.OK();
    }
}
