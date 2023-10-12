package com.poolaeem.poolaeem.solve.presentation.dto;

import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.validation.GradeValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradingRequest {

    public record WorkbookGrade(
            @NotBlank(message = GradeValidation.Message.SOLVED_USER_NAME_NOT_FOUND)
            String name,
            @NotNull(message = GradeValidation.Message.EMPTY_PROBLEMS)
            @NotEmpty(message = GradeValidation.Message.EMPTY_PROBLEMS)
            List<UserAnswer> problems
    ) {
    }
}
