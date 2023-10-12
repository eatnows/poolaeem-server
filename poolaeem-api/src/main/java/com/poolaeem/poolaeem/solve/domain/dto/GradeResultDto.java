package com.poolaeem.poolaeem.solve.domain.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradeResultDto {
    public record SolvedUsersReadParam(
        String reqUserId,
        String workbookId,
        String lastId,
        Pageable pageable
    ) {
        public SolvedUsersReadParam(String reqUserId, String workbookId, String lastId, int size) {
            this(reqUserId, workbookId, lastId, Pageable.ofSize(size));
        }
    }
}
