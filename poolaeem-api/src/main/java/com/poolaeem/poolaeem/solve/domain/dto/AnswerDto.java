package com.poolaeem.poolaeem.solve.domain.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AnswerDto {
    private String optionId;
    private List<String> answer = new ArrayList<>();
}
