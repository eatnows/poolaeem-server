package com.poolaeem.poolaeem.common.event.obj;

import com.poolaeem.poolaeem.common.file.FilePath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsPublisherFileEvent {

    @Getter
    public static class FileUploadEvent {
        private String fileId;
        private FilePath path;
        private MultipartFile fileObject;

        public FileUploadEvent(String fileId, FilePath path, MultipartFile fileObject) {
            this.fileId = fileId;
            this.path = path;
            this.fileObject = fileObject;
        }
    }

    @Getter
    public static class FileDeleteEvent {
        private String fileId;
        private FilePath path;
        public FileDeleteEvent(String fileId, FilePath path) {
            this.fileId = fileId;
            this.path = path;
        }
    }
}
