package com.poolaeem.poolaeem.question.domain.service;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.exception.workbook.WorkbookNotFoundException;
import com.poolaeem.poolaeem.question.application.WorkbookService;
import com.poolaeem.poolaeem.question.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.question.domain.validation.WorkbookValidation;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkbookServiceImpl implements WorkbookService {
    private final WorkbookRepository workbookRepository;

    public WorkbookServiceImpl(WorkbookRepository workbookRepository) {
        this.workbookRepository = workbookRepository;
    }

    @Transactional
    @Override
    public String createWorkbook(WorkbookDto.WorkbookCreateParam param) {
        validWorkbookLengthValidation(param.getName(), param.getDescription());

        int order = 1 + getLastOrderOfWorkbook(param.getUserId());
        Workbook workbook = new Workbook(
                param.getUserId(),
                param.getName(),
                param.getDescription(),
                param.getTheme(),
                order);
        Workbook saved = workbookRepository.save(workbook);
        return saved.getId();
    }

    @Transactional
    @Override
    public void updateWorkbook(WorkbookDto.WorkbookUpdateParam param) {
        validWorkbookLengthValidation(param.getName(), param.getDescription());

        Workbook workbook = getWorkbookEntity(param.getWorkbookId());

        validManage(workbook.getUserId(), param.getUserId());

        workbook.updateInfo(param.getName(), param.getDescription());
    }

    @Transactional(readOnly = true)
    @Override
    public WorkbookVo readWorkbookInfo(String workbookId, String reqUserId) {
        WorkbookVo workbook = workbookRepository.findDtoByIdAndIsDeletedFalse(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);
        validManage(workbook.getUserId(), reqUserId);

        return workbook;
    }

    @Transactional
    @Override
    public void increaseProblemCount(String workbookId) {
        workbookRepository.updateIncreaseProblemCountByWorkbookId(workbookId);
    }

    @Transactional
    @Override
    public void decreaseProblemCount(String workbookId) {
        workbookRepository.updateDecreaseProblemCountByWorkbookId(workbookId);
    }

    @Transactional(readOnly = true)
    @Override
    public WorkbookVo readWorkbookInfoForSolve(String workbookId) {
        return workbookRepository.findDtoByIdAndIsDeletedFalse(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    private Workbook getWorkbookEntity(String workbookdId) {
        return workbookRepository.findByIdAndIsDeletedFalse(workbookdId)
                .orElseThrow(WorkbookNotFoundException::new);
    }

    private void validManage(String ownerUserId, String reqUserId) {
        if (!ownerUserId.equals(reqUserId)) {
            throw new ForbiddenRequestException();
        }
    }

    private void validWorkbookLengthValidation(String name, String description) {
        if (name == null || description == null) {
            throw new BadRequestDataException();
        }
        if (name.length() < WorkbookValidation.NAME_MIN_LENGTH || name.length() > WorkbookValidation.NAME_MAX_LENGTH) {
            throw new BadRequestDataException();
        }
        if (description.length() < WorkbookValidation.DESCRIPTION_MIN_LENGTH || description.length() > WorkbookValidation.DESCRIPTION_MAX_LENGTH) {
            throw new BadRequestDataException();
        }
    }

    private int getLastOrderOfWorkbook(String userId) {
        return workbookRepository.findLastOrderByUserIdAndIsDeletedFalse(userId)
                .orElse(0);
    }
}
