package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.validation.WorkbookValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookRequest {

    @Getter
    public static class WorkbookCreateDto {
        @NotBlank
        @Length(min = WorkbookValidation.NAME_MIN_LENGTH, max = WorkbookValidation.NAME_MAX_LENGTH)
        private String name;
        @NotNull

        @Length(min = WorkbookValidation.DESCRIPTION_MIN_LENGTH, max = WorkbookValidation.DESCRIPTION_MAX_LENGTH)
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
        @NotBlank
        @Length(min = WorkbookValidation.NAME_MIN_LENGTH, max = WorkbookValidation.NAME_MAX_LENGTH)
        private String name;
        @NotNull
        @Length(min = WorkbookValidation.DESCRIPTION_MIN_LENGTH, max = WorkbookValidation.DESCRIPTION_MAX_LENGTH)
        private String description;

        public WorkbookUpdateDto(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
