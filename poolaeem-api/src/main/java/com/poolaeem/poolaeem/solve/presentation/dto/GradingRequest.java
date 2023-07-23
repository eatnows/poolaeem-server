package com.poolaeem.poolaeem.solve.presentation.dto;

import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.validation.GradeValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradingRequest {

    @Getter
    public static class  WorkbookGrade {
        @NotBlank(message = GradeValidation.Message.SOLVED_USER_NAME_NOT_FOUND)
        private String name;
        @NotNull(message = GradeValidation.Message.EMPTY_PROBLEMS)
        @NotEmpty(message = GradeValidation.Message.EMPTY_PROBLEMS)
        private List<UserAnswer> problems;

        public WorkbookGrade(String name, List<UserAnswer> problems) {
            this.name = name;
            this.problems = problems;
        }
    }
}
