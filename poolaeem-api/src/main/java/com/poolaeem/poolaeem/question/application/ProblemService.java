package com.poolaeem.poolaeem.question.application;

import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public interface ProblemService {
    void createProblem(ProblemDto.ProblemCreateParam param);

    ProblemVo readProblem(String userId, String problemId);

    void updateProblem(ProblemDto.ProblemUpdateParam param);

    void deleteProblem(String userId, String problemId);

    Slice<ProblemVo> readProblemList(String userId, String workbookId, int order, PageRequest pageable);
}
