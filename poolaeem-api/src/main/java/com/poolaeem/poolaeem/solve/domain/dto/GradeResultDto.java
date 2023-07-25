package com.poolaeem.poolaeem.solve.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradeResultDto {
    @Getter
    public static class SolvedUsersReadParam {
        private String reqUserId;
        private String workbookId;
        private String lastId;
        private Pageable pageable;

        public SolvedUsersReadParam(String reqUserId, String workbookId, String lastId, int size) {
            this.reqUserId = reqUserId;
            this.workbookId = workbookId;
            this.lastId = lastId;
            this.pageable = PageRequest.of(0, size);
        }
    }
}
