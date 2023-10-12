package com.poolaeem.poolaeem.solve.domain.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SolveDto {
    public record WorkbookGradingParam(
        String userId,
        String workbookId,
        String name,
        List<UserAnswer> problems
    ) {
    }
}
