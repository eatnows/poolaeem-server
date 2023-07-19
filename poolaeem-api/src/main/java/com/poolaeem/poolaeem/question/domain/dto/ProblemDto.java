package com.poolaeem.poolaeem.question.domain.dto;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemDto {

    @Getter
    public static class ProblemCreateParam {
        @NotBlank
        private String workbookId;
        @NotBlank
        private String reqUserId;
        @NotBlank
        @Length(min = ProblemValidation.QUESTION_MIN_LENGTH, max = ProblemValidation.QUESTION_MAX_LENGTH)
        private String question;
        @NotNull
        private ProblemType type;
        @Size(min = ProblemValidation.OPTION_MIN_SIZE, max = ProblemValidation.OPTION_MAX_SIZE)
        private List<ProblemOptionDto> options = new ArrayList<>();

        public ProblemCreateParam(String workbookId, String reqUserId, String question, ProblemType type, List<ProblemOptionDto> options) {
            this.workbookId = workbookId;
            this.reqUserId = reqUserId;
            this.question = question;
            this.type = type;
            this.options = options;
        }
    }

    @Getter
    public static class ProblemUpdateParam {
        private String problemId;
        private String reqUserId;
        private String question;
        private List<ProblemOptionDto> options;

        public ProblemUpdateParam(String problemId, String reqUserId, String question, List<ProblemOptionDto> options) {
            this.problemId = problemId;
            this.reqUserId = reqUserId;
            this.question = question;
            this.options = options;
        }
    }
}
