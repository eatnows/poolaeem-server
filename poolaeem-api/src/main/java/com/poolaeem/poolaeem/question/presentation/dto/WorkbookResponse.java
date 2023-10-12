package com.poolaeem.poolaeem.question.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookCreator;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookResponse {

    public record WorkbookCreate (
            String workbookId
    ) {
    }

    public record WorkbookInfoRead(
            String name,
            String description,
            int problemCount,
            int solvedCount,
            WorkbookTheme theme
    ) {
        public WorkbookInfoRead(WorkbookVo workbook) {
            this(workbook.name(), workbook.description(), workbook.problemCount(), workbook.solvedCount(), workbook.theme());
        }
    }

    public record SolveIntroductionRead(
            String name,
            String description,
            WorkbookTheme theme,
            WorkbookCreator creator,
            @JsonFormat(pattern = TimeComponent.DATETIME_PATTERN, timezone = TimeComponent.DEFAULT_TIMEZONE)
            ZonedDateTime createdAt,
            int problemCount,
            int solvedCount
    ) {
        public SolveIntroductionRead(WorkbookDto.SolveIntroductionRead info) {
            this(info.name(), info.description(), info.theme(), info.creator(), info.createdAt(), info.problemCount(), info.solvedCount());
        }
    }

    public record WorkbookListRead(
            List<WorkbookDto.WorkbookListRead> workbooks,
            Boolean hasNext
    ) {
        public WorkbookListRead(Slice<WorkbookDto.WorkbookListRead> result) {
            this(result.getContent(), result.hasNext());
        }
    }

    public record WorkbookUpdate (
            String name,
            String description,
            int problemCount,
            int solvedCount,
            WorkbookTheme theme
    ){
        public WorkbookUpdate(WorkbookVo workbook) {
            this(workbook.name(), workbook.description(), workbook.problemCount(), workbook.solvedCount(), workbook.theme());
        }
    }
}
