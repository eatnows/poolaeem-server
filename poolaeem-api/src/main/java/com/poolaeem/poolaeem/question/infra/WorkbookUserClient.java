package com.poolaeem.poolaeem.question.infra;

import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookCreator;

public interface WorkbookUserClient {
    WorkbookCreator readWorkbookCreator(String userId);
}
