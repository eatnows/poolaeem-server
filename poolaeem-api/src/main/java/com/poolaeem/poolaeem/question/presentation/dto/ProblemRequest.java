package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemRequest {
    @Getter
    public static class ProblemCreate {
        @NotBlank(message = ProblemValidation.Message.QUESTION_LENGTH)
        private String question;
        @NotNull(message = ProblemValidation.Message.PROBLEM_TYPE)
        private ProblemType type;
        private List<ProblemOptionDto> options = new ArrayList<>();

        public ProblemCreate(String question, ProblemType type, List<ProblemOptionDto> options) {
            this.question = question;
            this.type = type;
            this.options = options;
        }
    }

    @Getter
    public static class ProblemUpdate {
        @NotBlank(message = ProblemValidation.Message.QUESTION_LENGTH)
        private String question;
        @NotNull(message = ProblemValidation.Message.PROBLEM_TYPE)
        private ProblemType type;
        private List<ProblemOptionDto> options = new ArrayList<>();

        public ProblemUpdate(String question, ProblemType type, List<ProblemOptionDto> options) {
            this.question = question;
            this.type = type;
            this.options = options;
        }
    }
}
