package com.untact.file.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.file.domain.FileEntity;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long>, FileEntityCustomRepository {
}
