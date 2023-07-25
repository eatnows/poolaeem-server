package com.poolaeem.poolaeem.solve.application;

import com.poolaeem.poolaeem.solve.domain.dto.GradeResultDto;
import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import org.springframework.data.domain.Slice;

public interface GradeResultService {
    Slice<WorkbookResultVo> readSolvedHistoryOfWorkbook(GradeResultDto.SolvedUsersReadParam solvedUsersReadParam);
}
