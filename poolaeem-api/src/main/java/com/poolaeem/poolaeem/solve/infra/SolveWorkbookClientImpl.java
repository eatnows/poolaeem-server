package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.question.application.WorkbookService;
import org.springframework.stereotype.Service;

@Service
public class SolveWorkbookClientImpl implements SolveWorkbookClient {
    private final WorkbookService workbookService;

    public SolveWorkbookClientImpl(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }
}
