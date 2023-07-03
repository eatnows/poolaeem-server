package com.poolaeem.poolaeem.file.infra.repository;

import com.poolaeem.poolaeem.file.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {

    Optional<File> findByIdAndIsDeletedFalse(String fileId);
}
