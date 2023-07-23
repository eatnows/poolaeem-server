package com.poolaeem.poolaeem.question.application;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 문제집 이벤트 핸들러 테스트")
class WorkbookEventsHandlerTest {
    @InjectMocks
    private WorkbookEventsHandler workbookEventsHandler;
    @Mock
    private WorkbookService workbookService;

    @Test
    @DisplayName("문항수 증가 이벤트를 받아 문항수 증가를 요청한다.")
    void testIncreaseProblemCount() {
        String workbookId = "workbook-id";
        workbookEventsHandler.increaseProblemCount(new EventsPublisherWorkbookEvent.ProblemAddEvent(workbookId));

        verify(workbookService, times(1)).increaseProblemCount(workbookId);
    }

    @Test
    @DisplayName("문항수 감소 이벤트를 받아 문항수 감소를 요청한다.")
    void testDecreaseProblemCount() {
        String workbookId = "workbook-id";
        workbookEventsHandler.decreaseProblemCount(new EventsPublisherWorkbookEvent.ProblemDeleteEvent(workbookId));

        verify(workbookService, times(1)).decreaseProblemCount(workbookId);
    }

    @Test
    @DisplayName("풀이수 증가 이벤트를 받아 풀이수 증가를 요청한다.")
    void testIncreaseSolvedCount() {
        String workbookId = "workbook-id";
        workbookEventsHandler.increaseSolvedCount(new EventsPublisherWorkbookEvent.SolvedCountAddEvent(workbookId));

        verify(workbookService, times(1)).increaseSolvedCount(workbookId);
    }
}