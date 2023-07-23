package com.poolaeem.poolaeem.question.domain.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProblemValidation {
    public static final int QUESTION_MIN_LENGTH = 1;
    public static final int QUESTION_MAX_LENGTH = 100;
    public static final int OPTION_MIN_SIZE = 2;
    public static final int OPTION_MAX_SIZE = 10;
    public static final int OPTION_VALUE_MIN_LENGTH = 1;
    public static final int OPTION_VALUE_MAX_LENGTH = 100;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Message {
        public static final String PROBLEM_TYPE = "문항의 타입을 확인해주세요.";
        public static final String QUESTION_LENGTH = "문제의 길이를 확인해주세요.";
        public static final String OPTION_SIZE = "선택지의 개수를 확인해주세요.";
        public static final String OPTION_VALUE_LENGTH = "선택지의 길이를 확인해주세요.";
        public static final String CORRECT_OPTION_OR_WRONG_OPTION = "반드시 정답과 오답이 모두 존재해야 합니다.";
        public static final String PROBLEM_NOT_FOUND = "문항이 존재하지 않습니다.";
    }
}
