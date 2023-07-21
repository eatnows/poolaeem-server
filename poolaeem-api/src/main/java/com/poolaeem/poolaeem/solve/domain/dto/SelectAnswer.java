package com.poolaeem.poolaeem.solve.domain.dto;

import lombok.Getter;

@Getter
public class SelectAnswer {
    private String optionId;
    private String answer;

    public SelectAnswer(String optionId, String answer) {
        this.optionId = optionId;
        this.answer = answer;
    }
}
