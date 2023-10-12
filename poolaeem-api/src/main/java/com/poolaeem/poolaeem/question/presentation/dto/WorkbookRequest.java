package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.validation.WorkbookValidation;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookRequest {

    @Getter
    public static class WorkbookCreateDto {
        @NotNull(message = WorkbookValidation.Message.WORKBOOK_NAME_LENGTH)
        private String name;
        @NotNull(message = WorkbookValidation.Message.DESCRIPTION_LENGTH)
        private String description;
        private WorkbookTheme theme;

        public WorkbookCreateDto(String name, String description, WorkbookTheme theme) {
            this.name = name;
            this.description = description;
            this.theme = theme;
        }
    }

    @Getter
    public static class WorkbookUpdateDto {
        @NotNull(message = WorkbookValidation.Message.WORKBOOK_NAME_LENGTH)
        private String name;
        @NotNull(message = WorkbookValidation.Message.DESCRIPTION_LENGTH)
        private String description;
        @NotNull(message = WorkbookValidation.Message.WORKBOOK_THEME)
        private WorkbookTheme theme;

        public WorkbookUpdateDto(String name, String description, WorkbookTheme theme) {
            this.name = name;
            this.description = description;
            this.theme = theme;
        }
    }
}
