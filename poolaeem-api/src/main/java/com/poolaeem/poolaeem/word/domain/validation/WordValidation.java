package com.poolaeem.poolaeem.word.domain.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordValidation {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Message {
        public static final String NOT_SUPPORT_LANG = "해당 언어는 지원하지 않는 언어입니다.";
    }
}
