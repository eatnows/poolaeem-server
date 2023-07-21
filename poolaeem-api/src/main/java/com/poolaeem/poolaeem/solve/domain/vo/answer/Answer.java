package com.poolaeem.poolaeem.solve.domain.vo.answer;


import com.poolaeem.poolaeem.solve.domain.dto.SelectAnswer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {
    protected List<SelectAnswer> values;
    protected String value;

    public Answer(List<SelectAnswer> values) {
        this.values = values;
    }

    public List<SelectAnswer> getValues() {
        return values;
    }

    public String getValue() {
        return value;
    }
}
