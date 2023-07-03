package com.poolaeem.poolaeem.common.event;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FileEventsPublisher {
    private final ApplicationEventPublisher publisher;

    public FileEventsPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(EventsPublisherFileEvent.FileUploadEvent event) {
        assert event != null : "파일 업로드 이벤트에 오류가 발생했습니다.";

        publisher.publishEvent(event);
    }

    public void publish(EventsPublisherFileEvent.FileDeleteEvent event) {
        assert event != null : "파일 삭제 이벤트에 오류가 발생했습니다.";

        publisher.publishEvent(event);
    }
}
