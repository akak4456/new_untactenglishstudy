package com.untact.thesaurus.persistent;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.untact.thesaurus.domain.Thesaurus;

public class ThesaurusCustomRepositoryImpl extends QuerydslRepositorySupport implements ThesaurusCustomRepository {
	public ThesaurusCustomRepositoryImpl() {
		super(Thesaurus.class);
	}
}
