package com.poolaeem.poolaeem.question.presentation.dto;

import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookResponse {

    @Getter
    public static class WorkbookCreate {
        private String workbookId;

        public WorkbookCreate(String workbookId) {
            this.workbookId = workbookId;
        }
    }

    @Getter
    public static class WorkbookInfoRead {
        private String name;
        private String description;
        private int problemCount;
        private int solvedCount;
        private WorkbookTheme theme;

        public WorkbookInfoRead(WorkbookVo workbook) {
            this.name = workbook.getName();
            this.description = workbook.getDescription();
            this.problemCount = workbook.getProblemCount();
            this.solvedCount = workbook.getSolvedCount();
            this.theme = workbook.getTheme();
        }
    }
}
