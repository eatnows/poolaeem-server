package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.application.GradeResultService;
import com.poolaeem.poolaeem.solve.domain.dto.GradeResultDto;
import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import com.poolaeem.poolaeem.solve.infra.GradingWorkbookClient;
import com.poolaeem.poolaeem.solve.infra.repository.WorkbookResultRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GradeResultServiceImpl implements GradeResultService {
    private final WorkbookResultRepository workbookResultRepository;
    private final GradingWorkbookClient gradingWorkbookClient;

    public GradeResultServiceImpl(WorkbookResultRepository workbookResultRepository, GradingWorkbookClient gradingWorkbookClient) {
        this.workbookResultRepository = workbookResultRepository;
        this.gradingWorkbookClient = gradingWorkbookClient;
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<WorkbookResultVo> readSolvedHistoryOfWorkbook(GradeResultDto.SolvedUsersReadParam param) {
        validWorkbookManage(param.getReqUserId(), param.getWorkbookId());

        return workbookResultRepository.findAllDtoByWorkbookIdAndUserId(param.getWorkbookId(), param.getLastId(), param.getPageable());
    }

    private void validWorkbookManage(String reqUserId, String workbookId) {
        gradingWorkbookClient.validWorkbookManage(workbookId, reqUserId);
    }
}
