package com.poolaeem.poolaeem.workbook.domain.service;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.exception.workbook.WorkbookNotFoundException;
import com.poolaeem.poolaeem.workbook.application.WorkbookService;
import com.poolaeem.poolaeem.workbook.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.workbook.domain.entity.Workbook;
import com.poolaeem.poolaeem.workbook.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.workbook.domain.validation.WorkbookValidation;
import com.poolaeem.poolaeem.workbook.infra.repository.WorkbookRepository;
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
    public void createWorkbook(String userId, String name, String description) {
        validWorkbookLengthValidation(name, description);

        int order = 1 + getLastOrderOfWorkbook(userId);
        Workbook workbook = new Workbook(
                userId,
                name,
                description,
                WorkbookTheme.PINK,
                order);
        workbookRepository.save(workbook);
    }

    @Transactional
    @Override
    public void updateWorkbook(WorkbookDto.WorkbookUpdateParam param) {
        validWorkbookLengthValidation(param.getName(), param.getDescription());

        Workbook workbook = workbookRepository.findByIdAndIsDeletedFalse(param.getWorkbookId())
                .orElseThrow(WorkbookNotFoundException::new);

        validOwner(workbook.getUserId(), param.getUserId());

        workbook.updateInfo(param.getName(), param.getDescription());
    }

    private void validOwner(String ownerUserId, String reqUserId) {
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
