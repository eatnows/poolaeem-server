package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.application.SolveService;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;
import com.poolaeem.poolaeem.solve.infra.SolveWorkbookClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolveServiceImpl implements SolveService {
    private final SolveWorkbookClient solveWorkbookClient;

    public SolveServiceImpl(SolveWorkbookClient solveWorkbookClient) {
        this.solveWorkbookClient = solveWorkbookClient;
    }

    @Transactional(readOnly = true)
    @Override
    public WorkbookSolveDto.SolveInfoRead readSolveIntroduction(String workbookId) {
        return solveWorkbookClient.readWorkbookInfo(workbookId);
    }
}
