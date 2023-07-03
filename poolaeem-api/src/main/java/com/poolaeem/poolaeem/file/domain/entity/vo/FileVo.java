package com.poolaeem.poolaeem.file.domain.entity.vo;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FileVo {
    private String id;
    private String fileName;
    private long fileSize;
    private String fileExtension;
    private String path;
    private Boolean isDeleted;

    @QueryProjection
    public FileVo(String id, String fileName, long fileSize, String fileExtension, String path) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
        this.path = path;
    }
}
