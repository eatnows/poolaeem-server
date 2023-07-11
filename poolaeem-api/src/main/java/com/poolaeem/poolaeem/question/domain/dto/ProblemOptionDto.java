package com.poolaeem.poolaeem.question.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class ProblemOptionDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String optionId;
    @NotBlank
    @Length(min = ProblemValidation.OPTION_VALUE_MIN_LENGTH, max = ProblemValidation.OPTION_VALUE_MAX_LENGTH)
    private String value;
    private boolean isCorrect;

    public ProblemOptionDto() {
    }

    public ProblemOptionDto(String optionId, String value, boolean isCorrect) {
        this.optionId = optionId;
        this.value = value;
        this.isCorrect = isCorrect;
    }

    public ProblemOptionDto(String value, boolean isCorrect) {
        this.value = value;
        this.isCorrect = isCorrect;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getValue() {
        return value;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }
}
