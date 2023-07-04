package com.poolaeem.poolaeem.workbook.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookDto {

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
