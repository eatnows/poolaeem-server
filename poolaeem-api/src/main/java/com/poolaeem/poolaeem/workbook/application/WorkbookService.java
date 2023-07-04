package com.poolaeem.poolaeem.workbook.application;

import com.poolaeem.poolaeem.workbook.domain.dto.WorkbookDto;

public interface WorkbookService {
    void createWorkbook(String userId, String name, String description);

    void updateWorkbook(WorkbookDto.WorkbookUpdateParam param);

}
