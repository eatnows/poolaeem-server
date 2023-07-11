package com.poolaeem.poolaeem.common.event;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class WorkbookEventsPublisher {
    private final ApplicationEventPublisher publisher;

    public WorkbookEventsPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(EventsPublisherWorkbookEvent.ProblemAddEvent event) {
        if (event == null) throw new NullPointerException("문항 추가 이벤트가 존재하지 않습니다.");

        publisher.publishEvent(event);
    }

    public void publish(EventsPublisherWorkbookEvent.ProblemDeleteEvent event) {
        if (event == null) throw new NullPointerException("문항 삭제 이벤트가 존재하지 않습니다");

        publisher.publishEvent(event);
    }
}
