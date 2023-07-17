package com.poolaeem.poolaeem.question.application;

import com.poolaeem.poolaeem.question.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import org.springframework.transaction.annotation.Transactional;

public interface WorkbookService {
    String createWorkbook(WorkbookDto.WorkbookCreateParam param);

    void updateWorkbook(WorkbookDto.WorkbookUpdateParam param);

    WorkbookVo readWorkbookInfo(String workbookId, String userId);

    void increaseProblemCount(String workbookId);

    void decreaseProblemCount(String workbookId);

    WorkbookVo readWorkbookInfoForSolve(String workbookId);

    @Transactional(readOnly = true)
    WorkbookDto.SolveIntroductionRead readSolveIntroduction(String workbookId);
}
