package com.poolaeem.poolaeem.workbook.domain.dto;

import com.poolaeem.poolaeem.workbook.domain.entity.WorkbookTheme;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookDto {

    @Getter
    public static class WorkbookCreateParam {
        private String userId;
        private String name;
        private String description;
        private WorkbookTheme theme;

        public WorkbookCreateParam(String userId, String name, String description, WorkbookTheme theme) {
            this.userId = userId;
            this.name = name;
            this.description = description;
            this.theme = theme;
        }
    }

    @Getter
    public static final class WorkbookUpdateParam {
        private String workbookId;
        private String userId;
        private String name;
        private String description;

        public WorkbookUpdateParam(String workbookId, String userId, String name, String description) {
            this.workbookId = workbookId;
            this.userId = userId;
            this.name = name;
            this.description = description;
        }
    }
}
