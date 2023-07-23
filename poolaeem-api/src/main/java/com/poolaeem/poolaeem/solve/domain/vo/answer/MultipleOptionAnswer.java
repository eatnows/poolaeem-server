package com.poolaeem.poolaeem.solve.domain.vo.answer;

import java.util.List;

public class MultipleOptionAnswer extends Answer {

    public MultipleOptionAnswer(List<SelectAnswer> answers) {
        super(answers);
    }

    @Override
    public List<SelectAnswer> getValues() {
        return super.values;
    }
}
