package com.poolaeem.poolaeem.solve.domain.dto;

import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;

public record UserAnswer(
    String problemId,
    Answer answer
) {
}
