package com.poolaeem.poolaeem.question.application;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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

    @Async("asyncThreadPoolTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void increaseSolvedCount(EventsPublisherWorkbookEvent.SolvedCountAddEvent event) {
        workbookService.increaseSolvedCount(event.getWorkbookId());
    }
}
