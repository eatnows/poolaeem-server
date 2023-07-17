package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.question.application.WorkbookService;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookCreator;
import org.springframework.stereotype.Service;

@Service
public class SolveWorkbookClientImpl implements SolveWorkbookClient {
    private final WorkbookService workbookService;
    private final SolveUserClient solveUserClient;

    public SolveWorkbookClientImpl(WorkbookService workbookService, SolveUserClient solveUserClient) {
        this.workbookService = workbookService;
        this.solveUserClient = solveUserClient;
    }

    @Override
    public WorkbookSolveDto.SolveInfoRead readWorkbookInfo(String workbookId) {
        WorkbookVo workbook = workbookService.readWorkbookInfoForSolve(workbookId);
        WorkbookCreator creator = solveUserClient.readWorkbookCreator(workbook.getUserId());

        return new WorkbookSolveDto.SolveInfoRead(
                workbook.getId(),
                workbook.getName(),
                workbook.getDescription(),
                workbook.getTheme(),
                creator,
                workbook.getCreatedAt(),
                workbook.getProblemCount(),
                workbook.getSolvedCount()
        );
    }
}
