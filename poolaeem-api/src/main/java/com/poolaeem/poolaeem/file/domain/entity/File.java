package com.poolaeem.poolaeem.file.domain.entity;

import com.poolaeem.poolaeem.common.entity.BaseEntity;
import com.poolaeem.poolaeem.common.exception.file.FileNotFoundException;
import com.poolaeem.poolaeem.common.file.FilePath;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Entity
@Table(name = "file")
@NoArgsConstructor
public class File extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "fileSize")
    private long fileSize;

    @Column(name = "fileExtension")
    private String fileExtension;

    @Column(name = "path")
    private String path;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public File(String id, String fileName, long fileSize, String path) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.path = path;
        this.isDeleted = false;
    }

    public File(String id, FilePath path, MultipartFile file) {
        if (file == null) {
            throw new FileNotFoundException();
        }

        String[] split = file.getOriginalFilename().split("\\.");
        this.id = id;
        this.fileName = split[0];
        this.fileSize = file.getSize();
        this.fileExtension = split[1];
        this.path = path.getPath();
        this.isDeleted = false;
    }
}
