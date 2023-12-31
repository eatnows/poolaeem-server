package com.poolaeem.poolaeem.common.file;

import lombok.Getter;

@Getter
public enum FilePath {
    PROFILE_IMAGE("images/profile/")
    ;

    private String path;

    FilePath(String path) {
        this.path = path;
    }
}
