package com.poolaeem.poolaeem.workbook.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.workbook.application.WorkbookService;
import com.poolaeem.poolaeem.workbook.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.workbook.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.workbook.presentation.dto.WorkbookRequest;
import com.poolaeem.poolaeem.workbook.presentation.dto.WorkbookResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @PostMapping("/api/workbook")
    public ApiResponseDto<WorkbookResponse.WorkbookCreate> createWorkbook(@LoggedInUser UserVo user,
                                            @Valid @RequestBody WorkbookRequest.WorkbookCreateDto dto) {
        WorkbookDto.WorkbookCreateParam param =
                new WorkbookDto.WorkbookCreateParam(user.getId(), dto.getName(), dto.getDescription(), dto.getTheme());
        String workbookId = workbookService.createWorkbook(param);

        WorkbookResponse.WorkbookCreate response = new WorkbookResponse.WorkbookCreate(workbookId);
        return ApiResponseDto.OK(response);
    }

    @PutMapping("/api/workbooks/{workbookId}")
    public ApiResponseDto<?> updateWorkbook(@LoggedInUser UserVo user,
                                            @PathVariable(name = "workbookId") String workbookId,
                                            @Valid @RequestBody WorkbookRequest.WorkbookCreateDto dto) {
        WorkbookDto.WorkbookUpdateParam param = new WorkbookDto.WorkbookUpdateParam(workbookId, user.getId(), dto.getName(), dto.getDescription());
        workbookService.updateWorkbook(param);
        return ApiResponseDto.OK();
    }

    @GetMapping("/api/workbooks/{workbookId}")
    public ApiResponseDto<WorkbookResponse.WorkbookInfoRead> readWorkbookInfo(@LoggedInUser UserVo user,
                                              @PathVariable String workbookId) {
        WorkbookVo workbook = workbookService.readWorkbookInfo(workbookId, user.getId());

        WorkbookResponse.WorkbookInfoRead response = new WorkbookResponse.WorkbookInfoRead(workbook);
        return ApiResponseDto.OK(response);
    }
}
