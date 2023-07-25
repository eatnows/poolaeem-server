package com.poolaeem.poolaeem.solve.domain.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GradeValidation {
    public static final int SOLVED_USER_NAME_MAX_LENGTH = 30;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Message {
        public static final String SOLVED_USER_NAME_NOT_FOUND = "풀이자 별명이 존재하지않아 채점할 수 없습니다.";
        public static final String EMPTY_PROBLEMS = "풀이가 존재하지 않아 채점할 수 없습니다.";
        public static final String USER_NAME_LENGTH = "이름의 길이를 확인해주세요.";
    }
}
