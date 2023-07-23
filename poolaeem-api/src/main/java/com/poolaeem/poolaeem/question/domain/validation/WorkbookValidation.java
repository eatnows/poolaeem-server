package com.poolaeem.poolaeem.question.domain.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkbookValidation {
    public static final int NAME_MIN_LENGTH = 1;
    public static final int NAME_MAX_LENGTH = 30;
    public static final int DESCRIPTION_MIN_LENGTH = 0;
    public static final int DESCRIPTION_MAX_LENGTH = 300;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Message {
        public static final String WORKBOOK_NOT_FOUND = "문제집이 존재하지 않습니다.";
        public static final String WORKBOOK_NAME_LENGTH = "문제집의 이름을 확인해주세요.";
        public static final String DESCRIPTION_LENGTH = "문제집의 설명글을 확인해주세요.";
        public static final String WORKBOOK_THEME = "문제집의 테마를 확인해주세요.";
    }
}
