package com.untact.member.persistent;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.untact.member.domain.MemberEntity;

public class MemberEntityCustomRepositoryImpl extends QuerydslRepositorySupport
		implements MemberEntityCustomRepository {
	public MemberEntityCustomRepositoryImpl() {
		super(MemberEntity.class);
	}
}
