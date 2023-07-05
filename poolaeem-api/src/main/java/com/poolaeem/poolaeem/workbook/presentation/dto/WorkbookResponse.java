package com.poolaeem.poolaeem.workbook.presentation.dto;

import com.poolaeem.poolaeem.workbook.domain.entity.vo.WorkbookVo;
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

        public WorkbookInfoRead(WorkbookVo workbook) {
            this.name = workbook.getName();
            this.description = workbook.getDescription();
            this.problemCount = workbook.getProblemCount();
            this.solvedCount = workbook.getSolvedCount();
        }
    }
}
