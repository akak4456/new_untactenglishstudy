package com.untact.group.persistent;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.JPQLQuery;
import com.untact.group.domain.GroupEntity;
import com.untact.group.domain.QGroupEntity;
import com.untact.groupinclude.domain.QGroupInclude;

public class GroupEntityCustomRepositoryImpl extends QuerydslRepositorySupport implements GroupEntityCustomRepository {
	public GroupEntityCustomRepositoryImpl() {
		super(GroupEntity.class);
	}

	@Override
	public Page<GroupEntity> getPage(Pageable pageable) {
		final QGroupEntity group = QGroupEntity.groupEntity;
		final JPQLQuery<GroupEntity> query = from(group);
		return makePage(pageable,query);
	}

	@Override
	public Page<GroupEntity> getPageWithUserNumber(Pageable pageable, Long mno) {
		QGroupEntity group = QGroupEntity.groupEntity;
		QGroupInclude groupInclude =  QGroupInclude.groupInclude;
		JPQLQuery<GroupEntity> query = from(group);
		query.innerJoin(groupInclude).on(group.gno.eq(groupInclude.group.gno));
		query.where(groupInclude.member.mno.eq(mno));
		return makePage(pageable,query);
	}
	
	private Page<GroupEntity> makePage(Pageable pageable, JPQLQuery<GroupEntity> query){
		Long totalCount = query.fetchCount();
		List<GroupEntity> list = getQuerydsl().applyPagination(pageable, query).fetch();
		return new PageImpl<GroupEntity>(list,pageable,totalCount);
	}
}
