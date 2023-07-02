package com.poolaeem.poolaeem.file.infra.repository;

import com.poolaeem.poolaeem.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {

}
