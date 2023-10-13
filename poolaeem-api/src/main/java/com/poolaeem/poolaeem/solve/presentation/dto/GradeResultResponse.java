package com.poolaeem.poolaeem.solve.presentation.dto;

import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradeResultResponse {
    public record SolvedUsersRead(
            boolean hasNext,
            List<WorkbookResultVo> results
    ) {
        public SolvedUsersRead(Slice<WorkbookResultVo> results) {
            this(results.hasNext(), results.getContent());
        }
    }
}
