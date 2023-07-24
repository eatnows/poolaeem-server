package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;

import java.util.Map;

public interface GradingWorkbookClient {

    Map<String, ProblemGrading> getCorrectAnswers(String workbookId);

    void validWorkbookManage(String workbookId, String reqUserId);
}
