package com.poolaeem.poolaeem.question.domain.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ProblemValidation {
    public static final int QUESTION_MIN_LENGTH = 1;
    public static final int QUESTION_MAX_LENGTH = 100;
    public static final int OPTION_MIN_SIZE = 2;
    public static final int OPTION_MAX_SIZE = 10;
    public static final int OPTION_VALUE_MIN_LENGTH = 1;
    public static final int OPTION_VALUE_MAX_LENGTH = 100;
}
