package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.infra.SolveWorkbookClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 풀이 관련 테스트")
class SolveServiceImplTest {
    @InjectMocks
    private SolveServiceImpl solveService;
    @Mock
    SolveWorkbookClient solveWorkbookClient;


}