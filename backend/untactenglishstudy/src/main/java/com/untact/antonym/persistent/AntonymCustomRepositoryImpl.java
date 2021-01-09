package com.untact.antonym.persistent;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.untact.antonym.domain.Antonym;

public class AntonymCustomRepositoryImpl extends QuerydslRepositorySupport implements AntonymCustomRepository {
	public AntonymCustomRepositoryImpl() {
		super(Antonym.class);
	}
}
