package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemRequest;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemResponse;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

    @GetMapping("/api/problems/{problemId}")
    public ApiResponseDto<ProblemResponse.ProblemRead> readProblemInfo(@LoggedInUser UserVo user,
                                                                       @PathVariable String problemId) {
        ProblemVo problem = problemService.readProblem(user.getId(), problemId);

        ProblemResponse.ProblemRead response = new ProblemResponse.ProblemRead(problem);
        return ApiResponseDto.OK(response);
    }

    @PutMapping("/api/problems/{problemId}")
    public ApiResponseDto<?> updateProblem(@LoggedInUser UserVo user,
                                           @PathVariable String problemId,
                                           @Valid @RequestBody ProblemRequest.ProblemUpdate dto) {
        ProblemDto.ProblemUpdateParam param =
                new ProblemDto.ProblemUpdateParam(problemId, user.getId(), dto.getQuestion(), dto.getOptions());
        problemService.updateProblem(param);

        return ApiResponseDto.OK();
    }

    @DeleteMapping("/api/problems/{problemId}")
    public ApiResponseDto<?> deleteProblem(@LoggedInUser UserVo user,
                                           @PathVariable String problemId) {
        problemService.deleteProblem(user.getId(), problemId);
        return ApiResponseDto.OK();
    }

    @GetMapping("/api/workbooks/{workbookId}/problems")
    public ApiResponseDto<?> readProblemList(@LoggedInUser UserVo user,
                                             @PathVariable String workbookId,
                                             @RequestParam(defaultValue = "0") @Min(0) int order,
                                             @RequestParam(defaultValue = "20") @Max(100) int size) {
        Slice<ProblemVo> problems = problemService.readProblemList(user.getId(), workbookId, order, PageRequest.of(0, size));

        ProblemResponse.ProblemListRead response = new ProblemResponse.ProblemListRead(problems);
        return ApiResponseDto.OK(response);
    }
}
