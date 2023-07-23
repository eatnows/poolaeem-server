package com.poolaeem.poolaeem.common.event;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 문제집 이벤트 발행 테스트")
class WorkbookEventsPublisherTest {
    @InjectMocks
    private WorkbookEventsPublisher workbookEventsPublisher;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    @DisplayName("문항 추가 시 문제집에 문항개수 증가 이벤트가 발행된다.")
    void testIncreaseProblemCount() {
        EventsPublisherWorkbookEvent.ProblemAddEvent event =
                new EventsPublisherWorkbookEvent.ProblemAddEvent("workbook-id");
        workbookEventsPublisher.publish(event);

        verify(applicationEventPublisher, times(1)).publishEvent(event);
    }

    @Test
    @DisplayName("문항 삭제 시 문제집에 문항개수 감소 이벤트가 발행된다.")
    void testDecreaseProblemCount() {
        EventsPublisherWorkbookEvent.ProblemDeleteEvent event =
                new EventsPublisherWorkbookEvent.ProblemDeleteEvent("workbook-id");
        workbookEventsPublisher.publish(event);

        verify(applicationEventPublisher, times(1)).publishEvent(event);
    }

    @Test
    @DisplayName("문제 채점 시 문제집에 풀이자수 증가 이벤트가 발행된다.")
    void testIncreaseSolvedCount() {
        EventsPublisherWorkbookEvent.SolvedCountAddEvent event =
                new EventsPublisherWorkbookEvent.SolvedCountAddEvent("workbook-id");
        workbookEventsPublisher.publish(event);

        verify(applicationEventPublisher, times(1)).publishEvent(event);
    }
}