package com.untact.englishspelling.persistent;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.untact.englishspelling.domain.EnglishSpelling;

public class EnglishSpellingCustomRepositoryImpl extends QuerydslRepositorySupport
		implements EnglishSpellingCustomRepository {
	public EnglishSpellingCustomRepositoryImpl() {
		super(EnglishSpelling.class);
	}
}
