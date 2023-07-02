package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.file.application.FileDelete;
import com.poolaeem.poolaeem.file.application.FileService;
import com.poolaeem.poolaeem.file.application.FileUpload;
import com.poolaeem.poolaeem.file.domain.File;
import com.poolaeem.poolaeem.file.domain.FilePath;
import com.poolaeem.poolaeem.file.infra.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileUpload fileUpload;
    private final FileDelete fileDelete;

    public FileServiceImpl(FileRepository fileRepository, FileUpload fileUpload, FileDelete fileDelete) {
        this.fileRepository = fileRepository;
        this.fileUpload = fileUpload;
        this.fileDelete = fileDelete;
    }

    @Transactional
    @Override
    public void uploadNewFile(String fileId, FilePath path, MultipartFile fileObject) {
        File entity = new File(
                fileId,
                path,
                fileObject
        );

        fileRepository.save(entity);

        fileUpload.upload(fileObject, entity);
    }

    @Transactional
    @Override
    public void deleteUploadedFile(String fileId, FilePath path) {
        fileDelete.deleteUploadedFile(path.getPath() + fileId);
        fileRepository.deleteById(fileId);
    }
}
