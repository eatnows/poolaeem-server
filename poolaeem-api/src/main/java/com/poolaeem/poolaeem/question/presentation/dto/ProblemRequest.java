package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemRequest {
    public record ProblemCreate(
            @NotBlank(message = ProblemValidation.Message.QUESTION_LENGTH)
            String question,
            @NotNull(message = ProblemValidation.Message.PROBLEM_TYPE)
            ProblemType type,
            List<ProblemOptionDto> options
    ) {
    }

    public record ProblemUpdate(
            @NotBlank(message = ProblemValidation.Message.QUESTION_LENGTH)
            String question,
            @NotNull(message = ProblemValidation.Message.PROBLEM_TYPE)
            ProblemType type,
            List<ProblemOptionDto> options
    ) {
    }
}
