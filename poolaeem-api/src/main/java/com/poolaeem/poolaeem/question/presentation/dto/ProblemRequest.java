package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemRequest {
    @Getter
    public static class ProblemCreate {
        @NotBlank
        @Length(min = ProblemValidation.QUESTION_MIN_LENGTH, max = ProblemValidation.QUESTION_MAX_LENGTH)
        private String question;
        @Size.List(value = @Size(max = 10))
        private List<ProblemOptionDto> options = new ArrayList<>();

        public ProblemCreate(String question, List<ProblemOptionDto> options) {
            this.question = question;
            this.options = options;
        }
    }

    @Getter
    public static class ProblemUpdate {
        @NotBlank
        @Length(min = ProblemValidation.QUESTION_MIN_LENGTH, max = ProblemValidation.QUESTION_MAX_LENGTH)
        private String question;
        @Size.List(value = @Size(max = 10))
        private List<ProblemOptionDto> options = new ArrayList<>();

        public ProblemUpdate(String question, List<ProblemOptionDto> options) {
            this.question = question;
            this.options = options;
        }
    }
}
