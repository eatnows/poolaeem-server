package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemRequest {
    @Getter
    public static class ProblemCreate {
        private String question;
        @Size.List(value = @Size(max = 10))
        private List<ProblemOptionDto> options = new ArrayList<>();

        public ProblemCreate(String question, List<ProblemOptionDto> options) {
            this.question = question;
            this.options = options;
        }
    }
}
