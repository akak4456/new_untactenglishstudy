package com.untact.file.persistent;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.untact.file.domain.FileEntity;

public class FileEntityCustomRepositoryImpl extends QuerydslRepositorySupport implements FileEntityCustomRepository {
	public FileEntityCustomRepositoryImpl() {
		super(FileEntity.class);
	}
}
