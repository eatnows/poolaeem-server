package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.validation.WorkbookValidation;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookRequest {

    public record WorkbookCreateDto(
            @NotNull(message = WorkbookValidation.Message.WORKBOOK_NAME_LENGTH)
            String name,
            @NotNull(message = WorkbookValidation.Message.DESCRIPTION_LENGTH)
            String description,
            WorkbookTheme theme
    ) {
    }

    public record WorkbookUpdateDto(
            @NotNull(message = WorkbookValidation.Message.WORKBOOK_NAME_LENGTH)
            String name,
            @NotNull(message = WorkbookValidation.Message.DESCRIPTION_LENGTH)
            String description,
            @NotNull(message = WorkbookValidation.Message.WORKBOOK_THEME)
            WorkbookTheme theme
    ) {
    }
}
