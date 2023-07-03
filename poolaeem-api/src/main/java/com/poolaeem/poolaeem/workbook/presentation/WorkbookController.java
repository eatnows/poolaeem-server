package com.poolaeem.poolaeem.workbook.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.workbook.application.WorkbookService;
import com.poolaeem.poolaeem.workbook.presentation.dto.WorkbookRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
