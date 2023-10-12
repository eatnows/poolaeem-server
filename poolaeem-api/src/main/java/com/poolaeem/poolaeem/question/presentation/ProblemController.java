package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.annotation.LoggedInUserOnly;
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

    @LoggedInUserOnly
    @PostMapping("/api/workbooks/{workbookId}/problem")
    public ApiResponseDto<?> createProblem(@LoggedInUser UserVo user,
                                           @PathVariable String workbookId,
                                           @Valid @RequestBody ProblemRequest.ProblemCreate dto) {
        ProblemDto.ProblemCreateParam param =
                new ProblemDto.ProblemCreateParam(workbookId, user.id(), dto.question(), dto.type(), dto.options());
        problemService.createProblem(param);

        return ApiResponseDto.OK();
    }

    @LoggedInUserOnly
    @GetMapping("/api/problems/{problemId}")
    public ApiResponseDto<ProblemResponse.ProblemRead> readProblemInfo(@LoggedInUser UserVo user,
                                                                       @PathVariable String problemId) {
        ProblemVo problem = problemService.readProblem(user.id(), problemId);

        ProblemResponse.ProblemRead response = new ProblemResponse.ProblemRead(problem);
        return ApiResponseDto.OK(response);
    }

    @LoggedInUserOnly
    @PutMapping("/api/problems/{problemId}")
    public ApiResponseDto<ProblemResponse.ProblemRead> updateProblem(@LoggedInUser UserVo user,
                                           @PathVariable String problemId,
                                           @Valid @RequestBody ProblemRequest.ProblemUpdate dto) {
        ProblemDto.ProblemUpdateParam param =
                new ProblemDto.ProblemUpdateParam(problemId, user.id(), dto.question(), dto.options());
        problemService.updateProblem(param);
        ProblemVo problem = problemService.readProblem(user.id(), problemId);

        ProblemResponse.ProblemRead response = new ProblemResponse.ProblemRead(problem);
        return ApiResponseDto.OK(response);
    }

    @LoggedInUserOnly
    @DeleteMapping("/api/problems/{problemId}")
    public ApiResponseDto<?> deleteProblem(@LoggedInUser UserVo user,
                                           @PathVariable String problemId) {
        problemService.deleteProblem(user.id(), problemId);
        return ApiResponseDto.OK();
    }

    @LoggedInUserOnly
    @GetMapping("/api/workbooks/{workbookId}/problems")
    public ApiResponseDto<ProblemResponse.ProblemListRead> readProblemList(@LoggedInUser UserVo user,
                                                                           @PathVariable String workbookId,
                                                                           @RequestParam(defaultValue = "0") @Min(0) int order,
                                                                           @RequestParam(defaultValue = "20") @Max(100) int size) {
        Slice<ProblemVo> problems = problemService.readProblemList(user.id(), workbookId, order, PageRequest.of(0, size));

        ProblemResponse.ProblemListRead response = new ProblemResponse.ProblemListRead(problems);
        return ApiResponseDto.OK(response);
    }

    @GetMapping("/api/workbooks/{workbookId}/problems/solve")
    public ApiResponseDto<ProblemResponse.SolveProblemsRead> readSolveProblems(@PathVariable String workbookId,
                                                                               @RequestParam(defaultValue = "0") int order,
                                                                               @RequestParam(defaultValue = "20") @Max(20) int size) {
        Slice<ProblemVo> problems = problemService.readSolveProblems(workbookId, order, PageRequest.of(0, size));

        ProblemResponse.SolveProblemsRead response = new ProblemResponse.SolveProblemsRead(problems);
        return ApiResponseDto.OK(response);
    }
}
