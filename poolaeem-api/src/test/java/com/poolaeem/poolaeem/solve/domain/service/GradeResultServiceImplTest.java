package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.application.GradeResultService;
import com.poolaeem.poolaeem.solve.domain.dto.GradeResultDto;
import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import com.poolaeem.poolaeem.solve.infra.GradingWorkbookClient;
import com.poolaeem.poolaeem.solve.infra.repository.WorkbookResultRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 풀이 결과 테스트")
class GradeResultServiceImplTest {
    @InjectMocks
    private GradeResultServiceImpl gradeResultService;
    @Mock
    private WorkbookResultRepository workbookResultRepository;
    @Mock
    private GradingWorkbookClient gradingWorkbookClient;

    @Test
    @DisplayName("문제집의 풀이내역을 조회할 수 있다.")
    void testReadSolvedHistoryOfWorkbook() {
        GradeResultDto.SolvedUsersReadParam param =
                new GradeResultDto.SolvedUsersReadParam("user-id", "workbook-id", null, 20);

        List<WorkbookResultVo> mockList = List.of(
                new WorkbookResultVo("result-1", "꼬부기", 10, 9, ZonedDateTime.of(LocalDateTime.of(2023, 7, 24, 15, 13), ZoneId.of("Asia/Seoul"))),
                new WorkbookResultVo("result-2", "파이리", 10, 7, ZonedDateTime.of(LocalDateTime.of(2023, 7, 24, 15, 15), ZoneId.of("Asia/Seoul"))),
                new WorkbookResultVo("result-3", "이상해씨", 10, 10, ZonedDateTime.of(LocalDateTime.of(2023, 7, 24, 15, 16), ZoneId.of("Asia/Seoul")))
        );
        given(workbookResultRepository.findAllDtoByWorkbookIdAndUserId(param.getWorkbookId(), param.getLastId(), param.getPageable()))
                .willReturn(new SliceImpl<>(mockList, param.getPageable(), true));


        Slice<WorkbookResultVo> results = gradeResultService.readSolvedHistoryOfWorkbook(param);

        assertThat(results.getContent()).hasSize(mockList.size());
        assertThat(results.getContent().get(0).getResultId()).isEqualTo(mockList.get(0).getResultId());
        assertThat(results.getContent().get(1).getResultId()).isEqualTo(mockList.get(1).getResultId());
        assertThat(results.getContent().get(2).getResultId()).isEqualTo(mockList.get(2).getResultId());
    }
}