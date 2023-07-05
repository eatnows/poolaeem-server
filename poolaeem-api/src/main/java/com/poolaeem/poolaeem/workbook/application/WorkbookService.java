package com.poolaeem.poolaeem.workbook.application;

import com.poolaeem.poolaeem.workbook.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.workbook.domain.entity.vo.WorkbookVo;

public interface WorkbookService {
    String createWorkbook(WorkbookDto.WorkbookCreateParam param);

    void updateWorkbook(WorkbookDto.WorkbookUpdateParam param);

    WorkbookVo readWorkbookInfo(String workbookId, String userId);
}
