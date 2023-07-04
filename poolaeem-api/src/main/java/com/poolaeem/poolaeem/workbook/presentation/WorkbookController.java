package com.poolaeem.poolaeem.workbook.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.workbook.application.WorkbookService;
import com.poolaeem.poolaeem.workbook.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.workbook.presentation.dto.WorkbookRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @PostMapping("/api/workbook")
    public ApiResponseDto<?> createWorkbook(@LoggedInUser UserVo user,
                                            @Valid @RequestBody WorkbookRequest.WorkbookCreateDto dto) {
        workbookService.createWorkbook(user.getId(), dto.getName(), dto.getDescription());
        return ApiResponseDto.OK();
    }

    @PutMapping("/api/workbooks/{workbookId}")
    public ApiResponseDto<?> updateWorkbook(@LoggedInUser UserVo user,
                                            @PathVariable(name = "workbookId") String workbookId,
                                            @Valid @RequestBody WorkbookRequest.WorkbookCreateDto dto) {
        WorkbookDto.WorkbookUpdateParam param = new WorkbookDto.WorkbookUpdateParam(workbookId, user.getId(), dto.getName(), dto.getDescription());
        workbookService.updateWorkbook(param);
        return ApiResponseDto.OK();
    }
}
