package com.poolaeem.poolaeem.file.domain.service;

import com.poolaeem.poolaeem.file.application.FileDelete;
import com.poolaeem.poolaeem.file.application.FileService;
import com.poolaeem.poolaeem.file.application.FileUpload;
import com.poolaeem.poolaeem.file.domain.entity.File;
import com.poolaeem.poolaeem.common.file.FilePath;
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
        File entity = new File(fileId, path, fileObject);
        saveFileEntity(entity);
        fileUpload.upload(fileObject, entity);
    }

    private void saveFileEntity(File entity) {
        fileRepository.save(entity);
    }

    @Transactional
    @Override
    public void deleteUploadedFile(String fileId, FilePath path) {
        // TODO 추후에 소프트 삭제 후 배치를 돌려 실제 삭제할 수 있도록 개선
        fileDelete.deleteUploadedFile(path.getPath() + fileId);
        hardDeleteFileEntity(fileId);
    }

    private void hardDeleteFileEntity(String fileId) {
        fileRepository.deleteById(fileId);
    }
}
