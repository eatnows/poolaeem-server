package com.poolaeem.poolaeem.question.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ProblemOptionDto(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String optionId,
    @NotBlank
    @Length(min = ProblemValidation.OPTION_VALUE_MIN_LENGTH, max = ProblemValidation.OPTION_VALUE_MAX_LENGTH)
    String value,
    boolean isCorrect
) {

    public ProblemOptionDto(String value, boolean isCorrect) {
        this(null, value, isCorrect);
    }
}
