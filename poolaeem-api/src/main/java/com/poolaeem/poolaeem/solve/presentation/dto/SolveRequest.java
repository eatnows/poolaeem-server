package com.poolaeem.poolaeem.solve.presentation.dto;

import com.poolaeem.poolaeem.solve.domain.UserAnswer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SolveRequest {

    @Getter
    public static class WorkbookSolve {
        private String name;
        private List<UserAnswer> problems;
    }
}
