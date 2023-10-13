package com.poolaeem.poolaeem.question.domain.dto;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemDto {
    public record ProblemCreateParam(
        @NotBlank
        String workbookId,
        @NotBlank
        String reqUserId,
        @NotBlank
        @Length(min = ProblemValidation.QUESTION_MIN_LENGTH, max = ProblemValidation.QUESTION_MAX_LENGTH)
        String question,
        @NotNull
        ProblemType type,
        @Size(min = ProblemValidation.OPTION_MIN_SIZE, max = ProblemValidation.OPTION_MAX_SIZE)
        List<ProblemOptionDto> options
    ) {
    }

    public record ProblemUpdateParam(
            String problemId,
            String reqUserId,
            String question,
            List<ProblemOptionDto> options
    ) {
    }
}
