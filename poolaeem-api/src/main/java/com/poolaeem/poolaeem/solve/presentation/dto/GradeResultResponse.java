package com.poolaeem.poolaeem.solve.presentation.dto;

import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradeResultResponse {
    @Getter
    public static class SolvedUsersRead {
        private boolean hasNext;
        private List<WorkbookResultVo> results;

        public SolvedUsersRead(Slice<WorkbookResultVo> results) {
            this.hasNext = results.hasNext();
            this.results = results.getContent();
        }
    }
}
