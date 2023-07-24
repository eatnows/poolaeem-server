package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.annotation.LoggedInUserOnly;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.question.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.question.presentation.dto.WorkbookRequest;
import com.poolaeem.poolaeem.question.presentation.dto.WorkbookResponse;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.question.application.WorkbookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @LoggedInUserOnly
    @PostMapping("/api/workbook")
    public ApiResponseDto<WorkbookResponse.WorkbookCreate> createWorkbook(@LoggedInUser UserVo user,
                                                                          @Valid @RequestBody WorkbookRequest.WorkbookCreateDto dto) {
        WorkbookDto.WorkbookCreateParam param =
                new WorkbookDto.WorkbookCreateParam(user.getId(), dto.getName(), dto.getDescription(), dto.getTheme());
        String workbookId = workbookService.createWorkbook(param);

        WorkbookResponse.WorkbookCreate response = new WorkbookResponse.WorkbookCreate(workbookId);
        return ApiResponseDto.OK(response);
    }

    @LoggedInUserOnly
    @PutMapping("/api/workbooks/{workbookId}")
    public ApiResponseDto<?> updateWorkbook(@LoggedInUser UserVo user,
                                            @PathVariable(name = "workbookId") String workbookId,
                                            @Valid @RequestBody WorkbookRequest.WorkbookUpdateDto dto) {
        WorkbookDto.WorkbookUpdateParam param = new WorkbookDto.WorkbookUpdateParam(workbookId, user.getId(), dto.getName(), dto.getDescription());
        workbookService.updateWorkbook(param);
        return ApiResponseDto.OK();
    }

    @LoggedInUserOnly
    @GetMapping("/api/workbooks/{workbookId}")
    public ApiResponseDto<WorkbookResponse.WorkbookInfoRead> readWorkbookInfo(@LoggedInUser UserVo user,
                                              @PathVariable String workbookId) {
        WorkbookVo workbook = workbookService.readWorkbookInfo(workbookId, user.getId());

        WorkbookResponse.WorkbookInfoRead response = new WorkbookResponse.WorkbookInfoRead(workbook);
        return ApiResponseDto.OK(response);
    }

    @GetMapping("/api/workbooks/{workbookId}/solve/introduction")
    public ApiResponseDto<WorkbookResponse.SolveIntroductionRead> readWorkbookSolveIntroduction(@PathVariable String workbookId) {
        WorkbookDto.SolveIntroductionRead info = workbookService.readSolveIntroduction(workbookId);
        WorkbookResponse.SolveIntroductionRead response = new WorkbookResponse.SolveIntroductionRead(info);
        return ApiResponseDto.OK(response);
    }
}
