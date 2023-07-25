package com.poolaeem.poolaeem.question.domain.service;

import com.poolaeem.poolaeem.common.exception.common.EntityNotFoundException;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.application.WorkbookService;
import com.poolaeem.poolaeem.question.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookCreator;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.question.domain.validation.WorkbookValidation;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import com.poolaeem.poolaeem.question.infra.WorkbookUserClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkbookServiceImpl implements WorkbookService {
    private final WorkbookRepository workbookRepository;
    private final WorkbookUserClient workbookUserClient;
    private final ProblemService problemService;

    public WorkbookServiceImpl(WorkbookRepository workbookRepository, WorkbookUserClient workbookUserClient, ProblemService problemService) {
        this.workbookRepository = workbookRepository;
        this.workbookUserClient = workbookUserClient;
        this.problemService = problemService;
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
        WorkbookVo workbook = getWorkbookVo(workbookId);
        validManage(workbook.getUserId(), reqUserId);

        return workbook;
    }

    private WorkbookVo getWorkbookVo(String workbookId) {
        return workbookRepository.findDtoByIdAndIsDeletedFalse(workbookId)
                .orElseThrow(() -> new EntityNotFoundException(WorkbookValidation.Message.WORKBOOK_NOT_FOUND));
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
    public WorkbookDto.SolveIntroductionRead readSolveIntroduction(String workbookId) {
        WorkbookVo workbook = getWorkbookVo(workbookId);
        WorkbookCreator creator = workbookUserClient.readWorkbookCreator(workbook.getUserId());

        return new WorkbookDto.SolveIntroductionRead (
                workbook.getId(),
                workbook.getName(),
                workbook.getDescription(),
                workbook.getTheme(),
                creator,
                workbook.getCreatedAt(),
                workbook.getProblemCount(),
                workbook.getSolvedCount()
        );
    }

    @Transactional
    @Override
    public void increaseSolvedCount(String workbookId) {
        workbookRepository.updateIncreaseSolvedCountByWorkbookId(workbookId);
    }

    @Transactional(readOnly = true)
    @Override
    public void validWorkbookManage(String workbookId, String reqUserId) {
        WorkbookVo workbookVo = getWorkbookVo(workbookId);

        String creator = workbookVo.getUserId();
        validManage(creator, reqUserId);
    }

    @Transactional
    @Override
    public void deleteWorkbook(String reqUserId, String workbookId) {
        Workbook workbook = getWorkbookEntity(workbookId);
        validManage(workbook.getUserId(), reqUserId);

        problemService.softDeleteAllProblemsAndOptions(workbook);
        workbook.delete();
    }

    private Workbook getWorkbookEntity(String workbookId) {
        return workbookRepository.findByIdAndIsDeletedFalse(workbookId)
                .orElseThrow(() -> new EntityNotFoundException(WorkbookValidation.Message.WORKBOOK_NOT_FOUND));
    }

    private void validManage(String ownerUserId, String reqUserId) {
        if (!ownerUserId.equals(reqUserId)) {
            throw new ForbiddenRequestException(WorkbookValidation.Message.WORKBOOK_FORBIDDEN);
        }
    }

    private void validWorkbookLengthValidation(String name, String description) {
        if (name == null || description == null) {
            throw new BadRequestDataException();
        }
        if (name.length() < WorkbookValidation.NAME_MIN_LENGTH || name.length() > WorkbookValidation.NAME_MAX_LENGTH) {
            throw new BadRequestDataException(WorkbookValidation.Message.WORKBOOK_NAME_LENGTH);
        }
        if (description.length() < WorkbookValidation.DESCRIPTION_MIN_LENGTH || description.length() > WorkbookValidation.DESCRIPTION_MAX_LENGTH) {
            throw new BadRequestDataException(WorkbookValidation.Message.DESCRIPTION_LENGTH);
        }
    }

    private int getLastOrderOfWorkbook(String userId) {
        return workbookRepository.findLastOrderByUserIdAndIsDeletedFalse(userId)
                .orElse(0);
    }
}
