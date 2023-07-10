package com.poolaeem.poolaeem.common.event.obj;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsPublisherWorkbookEvent {
    @Getter
    public static class ProblemAddEvent {
        private String workbookId;

        public ProblemAddEvent(String workbookId) {
            this.workbookId = workbookId;
        }
    }
}
