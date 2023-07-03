package com.poolaeem.poolaeem.integration.file;

import com.poolaeem.poolaeem.file.application.FileDelete;
import com.poolaeem.poolaeem.integration.base.BaseLocalStackTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@DisplayName("통합: 파일 삭제 테스트")
class FileDeleteImplTest extends BaseLocalStackTest {

    @Autowired
    private FileDelete fileDelete;

    @Test
    @DisplayName("S3에 업로드된 파일을 삭제할 수 있다.")
    void deleteUploadedFile() {
        String key = "images/profile/poolaeem_logo.png";

        fileDelete.deleteUploadedFile(key);
    }
}