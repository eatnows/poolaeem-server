package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.solve.application.SolveService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SolveController {
    private final SolveService solveService;

    public SolveController(SolveService solveService) {
        this.solveService = solveService;
    }


}
