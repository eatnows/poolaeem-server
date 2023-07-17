package com.poolaeem.poolaeem.question.domain.service;

import com.poolaeem.poolaeem.common.event.WorkbookEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import com.poolaeem.poolaeem.common.exception.common.EntityNotFoundException;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.exception.workbook.WorkbookNotFoundException;
import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;
import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import com.poolaeem.poolaeem.question.domain.validation.ProblemValidation;
import com.poolaeem.poolaeem.question.infra.repository.ProblemOptionRepository;
import com.poolaeem.poolaeem.question.infra.repository.ProblemRepository;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;
    private final ProblemOptionRepository problemOptionRepository;
    private final WorkbookEventsPublisher workbookEventsPublisher;

    public ProblemServiceImpl(ProblemRepository problemRepository, WorkbookRepository workbookRepository, ProblemOptionRepository problemOptionRepository, WorkbookEventsPublisher workbookEventsPublisher) {
        this.problemRepository = problemRepository;
        this.workbookRepository = workbookRepository;
        this.problemOptionRepository = problemOptionRepository;
        this.workbookEventsPublisher = workbookEventsPublisher;
    }

    @Transactional
    @Override
    public void createProblem(ProblemDto.ProblemCreateParam param) {
        validProblemQuestion(param.getQuestion());
        validProblemOption(param.getOptions());

        Problem problem = saveProblem(param);
        saveOptions(problem, param.getOptions());

        increaseProblemCount(param);
    }

    @Transactional(readOnly = true)
    @Override
    public ProblemVo readProblem(String userId, String problemId) {
        Problem problem = problemRepository.findByIdAndIsDeletedFalseAndUserId(problemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("문항이 존재하지 않습니다"));

        List<ProblemOptionVo> options = problem.getOptions().stream()
                .map(o -> new ProblemOptionVo(o.getId(), o.getValue(), o.getIsCorrect()))
                .toList();

        return new ProblemVo(problem.getId(), null, problem.getQuestion(), problem.getType(), problem.getTimeout(), options);
    }

    @Transactional
    @Override
    public void updateProblem(ProblemDto.ProblemUpdateParam param) {
        validProblemQuestion(param.getQuestion());
        validProblemOption(param.getOptions());

        Problem problem = problemRepository.findByIdAndIsDeletedFalseAndUserId(param.getProblemId(), param.getReqUserId())
                .orElseThrow(() -> new EntityNotFoundException("문항이 존재하지 않습니다"));

        problem.updateQuestion(param.getQuestion());
        problem.updateOptionCount(param.getOptions().size());
        updateProblemOptions(problem, param.getOptions());
    }

    @Transactional
    @Override
    public void deleteProblem(String userId, String problemId) {
        Problem problem = problemRepository.findByIdAndIsDeletedFalseAndUserId(problemId, userId)
                .orElseThrow(EntityNotFoundException::new);

        problemOptionRepository.softDeleteAllByProblem(problem);
        problemRepository.softDelete(problem);

        decreaseProblemCount(problem.getWorkbook().getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<ProblemVo> readProblemList(String userId, String workbookId, int order, Pageable pageable) {
        WorkbookVo workbook = workbookRepository.findDtoByIdAndIsDeletedFalse(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);
        validWorkbookManage(workbook.getUserId(), userId);

        return problemRepository.findAllByWorkbookIdAndIsDeletedFalse(workbookId, order, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public SliceImpl<ProblemVo> readSolveProblems(String workbookId, int order) {
        Pageable pageable = PageRequest.of(0, 20);
        SliceImpl<ProblemVo> problems = problemRepository.findAllDtoByWorkbookIdAndIsDeletedFalse(workbookId, order, pageable);

        addOptionsInProblem(problems.getContent());

        return problems;
    }

    private void addOptionsInProblem(List<ProblemVo> problems) {
        List<String> problemIds = problems.stream().map(ProblemVo::getProblemId).toList();

        List<ProblemOptionVo> options = problemOptionRepository.findAllDtoByProblemIdInAndIsDeletedFalse(problemIds);
        Collections.shuffle(options);

        Map<String, ProblemVo> map = problems.stream().collect(Collectors.toMap(ProblemVo::getProblemId, Function.identity()));

        for (ProblemOptionVo option : options) {
            ProblemVo problemVo = map.get(option.getProblemId());
            option.removeProblemId();
            problemVo.addOption(option);
        }
    }

    private void decreaseProblemCount(String workbookId) {
        workbookEventsPublisher.publish(new EventsPublisherWorkbookEvent.ProblemDeleteEvent(workbookId));
    }

    private void updateProblemOptions(Problem problem, List<ProblemOptionDto> reqOptions) {
        List<ProblemOption> options = problemOptionRepository.findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(problem.getId());

        softDeleteOptions(options, reqOptions);
        updateOptionsOrder(options, reqOptions);
        addNewOptions(problem, reqOptions);
    }

    private void updateOptionsOrder(List<ProblemOption> dbOptions, List<ProblemOptionDto> reqOptions) {
        Map<String, ProblemOption> dbOptionMap = dbOptions.stream()
                .collect(Collectors.toMap(ProblemOption::getId, Function.identity()));

        for (int i = 0; i < reqOptions.size(); i++) {
            ProblemOptionDto option = reqOptions.get(i);
            if (option.getOptionId() == null) continue;

            ProblemOption problemOption = dbOptionMap.get(option.getOptionId());
            problemOption.updateOrder(i + 1);
        }
    }

    private void addNewOptions(Problem problem, List<ProblemOptionDto> reqOptions) {
        List<ProblemOption> newList = new ArrayList<>();
        for (int i = 0; i < reqOptions.size(); i++) {
            ProblemOptionDto option = reqOptions.get(i);
            if (option.getOptionId() != null) continue;
            newList.add(new ProblemOption(problem, option.getValue(), option.getIsCorrect(), i + 1));
        }

        problemOptionRepository.saveAll(newList);
    }

    private void softDeleteOptions(List<ProblemOption> dbOptions, List<ProblemOptionDto> reqOptions) {
        Map<String, ProblemOptionDto> reqOptionMap = reqOptions.stream()
                .filter(option -> option.getOptionId() != null)
                .collect(Collectors.toMap(ProblemOptionDto::getOptionId, Function.identity()));

        List<ProblemOption> deleteList = new ArrayList<>();
        for (ProblemOption dbOption : dbOptions) {
            if (reqOptionMap.get(dbOption.getId()) == null) {
                deleteList.add(dbOption);
            }
        }

        problemOptionRepository.softDeleteAllByIdIn(deleteList);
    }

    private void increaseProblemCount(ProblemDto.ProblemCreateParam param) {
        workbookEventsPublisher.publish(new EventsPublisherWorkbookEvent.ProblemAddEvent(param.getWorkbookId()));
    }

    private void validProblemOption(List<ProblemOptionDto> options) {
        if (options.size() > ProblemValidation.OPTION_MAX_SIZE ||
                options.size() < ProblemValidation.OPTION_MIN_SIZE) {
            throw new BadRequestDataException();
        }
        boolean existCorrect = false;
        boolean existWrong = false;
        for (ProblemOptionDto option : options) {
            if (option.getValue().length() > ProblemValidation.OPTION_VALUE_MAX_LENGTH ||
                    option.getValue().length() < ProblemValidation.OPTION_VALUE_MIN_LENGTH) {
                throw new BadRequestDataException("선택지의 값 길이를 확인해주세요");
            }

            if (option.getIsCorrect()) {
                existCorrect = true;
            } else {
                existWrong = true;
            }
        }

        if (!existCorrect || !existWrong) {
            throw new BadRequestDataException("반드시 오답과 정답이 하나씩 존재해야 합니다.");
        }
    }

    private void validProblemQuestion(String question) {
        if (question.length() > ProblemValidation.QUESTION_MAX_LENGTH ||
                question.length() < ProblemValidation.QUESTION_MIN_LENGTH) {
            throw new BadRequestDataException();
        }
    }

    private void saveOptions(Problem problem, List<ProblemOptionDto> optionDtos) {
        List<ProblemOption> options = new ArrayList<>();
        int order = 1;
        for (ProblemOptionDto o : optionDtos) {
            options.add(new ProblemOption(problem, o.getValue(), o.getIsCorrect(), order++));
        }

        problemOptionRepository.saveAll(options);
    }

    private Problem saveProblem(ProblemDto.ProblemCreateParam param) {
        Workbook workbook = getWorkbook(param.getWorkbookId(), param.getReqUserId());
        int order = lastProblemOrder(workbook) + 1;
        Problem problem = new Problem(workbook, param.getQuestion(), param.getType(), param.getOptions().size(), order);
        return problemRepository.save(problem);
    }

    private int lastProblemOrder(Workbook workbook) {
        return problemRepository.findLastOrderByWorkbookAndIsDeleteFalse(workbook)
                .orElse(0);
    }

    private Workbook getWorkbook(String workbookId, String reqUserId) {
        Workbook workbook = workbookRepository.findByIdAndIsDeletedFalse(workbookId)
                .orElseThrow(WorkbookNotFoundException::new);

        validWorkbookManage(workbook.getUserId(), reqUserId);
        return workbook;
    }

    private void validWorkbookManage(String userId, String reqUserId) {
        if (!userId.equals(reqUserId)) {
            throw new ForbiddenRequestException();
        }
    }
}
