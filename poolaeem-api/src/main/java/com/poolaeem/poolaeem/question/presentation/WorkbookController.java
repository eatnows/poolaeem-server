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
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
                new WorkbookDto.WorkbookCreateParam(user.getId(), dto.name(), dto.description(), dto.theme());
        String workbookId = workbookService.createWorkbook(param);

        WorkbookResponse.WorkbookCreate response = new WorkbookResponse.WorkbookCreate(workbookId);
        return ApiResponseDto.OK(response);
    }

    @LoggedInUserOnly
    @PutMapping("/api/workbooks/{workbookId}")
    public ApiResponseDto<WorkbookResponse.WorkbookUpdate> updateWorkbook(@LoggedInUser UserVo user,
                                            @PathVariable(name = "workbookId") String workbookId,
                                            @Valid @RequestBody WorkbookRequest.WorkbookUpdateDto dto) {
        WorkbookDto.WorkbookUpdateParam param = new WorkbookDto.WorkbookUpdateParam(workbookId, user.getId(), dto.name(), dto.description(), dto.theme());
        workbookService.updateWorkbook(param);

        WorkbookVo workbookInfo = workbookService.readWorkbookInfo(workbookId, user.getId());
        WorkbookResponse.WorkbookUpdate response = new WorkbookResponse.WorkbookUpdate(workbookInfo);
        return ApiResponseDto.OK(response);
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

    @LoggedInUserOnly
    @DeleteMapping("/api/workbooks/{workbookId}")
    public ApiResponseDto<?> deleteWorkbook(@LoggedInUser UserVo user,
                                            @PathVariable String workbookId) {
        workbookService.deleteWorkbook(user.getId(), workbookId);
        return ApiResponseDto.OK();
    }

    @LoggedInUserOnly
    @GetMapping("/api/workbooks")
    public ApiResponseDto<WorkbookResponse.WorkbookListRead> readMyWorkbooks(@LoggedInUser UserVo user,
                                                                             @RequestParam(defaultValue = "10") @Max(100) int size,
                                                                             @RequestParam(required = false) String lastId) {
        Slice<WorkbookDto.WorkbookListRead> results =
                workbookService.readMyWorkbooks(user.getId(), PageRequest.of(0, size), lastId);

        WorkbookResponse.WorkbookListRead response = new WorkbookResponse.WorkbookListRead(results);
        return ApiResponseDto.OK(response);
    }
}
