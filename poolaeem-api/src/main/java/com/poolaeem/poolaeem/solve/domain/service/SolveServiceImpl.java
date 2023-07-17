package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.application.SolveService;
import com.poolaeem.poolaeem.solve.infra.SolveWorkbookClient;
import org.springframework.stereotype.Service;

@Service
public class SolveServiceImpl implements SolveService {
    private final SolveWorkbookClient solveWorkbookClient;

    public SolveServiceImpl(SolveWorkbookClient solveWorkbookClient) {
        this.solveWorkbookClient = solveWorkbookClient;
    }
}
