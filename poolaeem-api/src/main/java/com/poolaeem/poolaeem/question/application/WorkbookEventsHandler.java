package com.poolaeem.poolaeem.question.application;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WorkbookEventsHandler {
    private final WorkbookService workbookService;

    public WorkbookEventsHandler(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @EventListener(classes = EventsPublisherWorkbookEvent.ProblemAddEvent.class)
    public void increaseProblemCount(EventsPublisherWorkbookEvent.ProblemAddEvent event) {
        workbookService.increaseProblemCount(event.getWorkbookId());
    }

    @EventListener(classes = EventsPublisherWorkbookEvent.ProblemDeleteEvent.class)
    public void decreaseProblemCount(EventsPublisherWorkbookEvent.ProblemDeleteEvent event) {
        workbookService.decreaseProblemCount(event.getWorkbookId());
    }
}
