package com.poolaeem.poolaeem.file.application;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FileEventsHandler {

    private final FileService fileService;

    public FileEventsHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @EventListener(classes = EventsPublisherFileEvent.FileUploadEvent.class)
    private void uploadNewFile(EventsPublisherFileEvent.FileUploadEvent event) {
        fileService.uploadNewFile(event.getFileId(), event.getPath(), event.getFileObject());
    }

    @EventListener(classes = EventsPublisherFileEvent.FileDeleteEvent.class)
    private void deleteUploadedFile(EventsPublisherFileEvent.FileDeleteEvent event) {
        fileService.deleteUploadedFile(event.getFileId(), event.getPath());
    }
}
