package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookAuthor;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;
import com.poolaeem.poolaeem.solve.infra.SolveWorkbookClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 풀이 관련 테스트")
class SolveServiceImplTest {
    @InjectMocks
    private SolveServiceImpl solveService;
    @Mock
    SolveWorkbookClient solveWorkbookClient;

    @Test
    @DisplayName("문제집 풀이 정보를 조회할 수 있다.")
    void testReadWorkbookSolveInfo() throws Exception {
        String workbookId = "workbook-id";
        given(solveWorkbookClient.readWorkbookInfo(workbookId))
                .willReturn(new WorkbookSolveDto.SolveInfoRead(
                        workbookId,
                        "문제집",
                        "단어장",
                        new WorkbookAuthor(
                                "만든이",
                                "https://poolaeem.com/profile/test/123"
                        ),
                        ZonedDateTime.now(),
                        20,
                        2
                ));

        WorkbookSolveDto.SolveInfoRead info = solveService.readSolveIntroduction(workbookId);

        assertThat(info.getWorkbookId()).isEqualTo(workbookId);
        assertThat(info.getAuthor().getName()).isEqualTo("만든이");
    }
}