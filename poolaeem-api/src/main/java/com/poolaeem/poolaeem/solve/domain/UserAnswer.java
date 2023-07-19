package com.poolaeem.poolaeem.solve.domain;

import com.poolaeem.poolaeem.solve.domain.dto.AnswerDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserAnswer {
    private String problemId;
    private List<AnswerDto> answers = new ArrayList<>();
}
