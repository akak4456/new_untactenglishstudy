package com.untact.phrase.persistent;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.untact.phrase.domain.Phrase;

public class PhraseCustomRepositoryImpl extends QuerydslRepositorySupport implements PhraseCustomRepository {
	public PhraseCustomRepositoryImpl() {
		super(Phrase.class);
	}
}
