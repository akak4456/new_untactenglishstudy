package com.untact.groupinclude.persistent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.untact.group.domain.QGroupEntity;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.domain.QGroupInclude;
import com.untact.groupinclude.domain.WhichStatus;
import com.untact.member.domain.MemberEntity;
import com.untact.member.domain.QMemberEntity;
import com.untact.vo.MemberManageVO;

public class GroupIncludeCustomRepositoryImpl extends QuerydslRepositorySupport
		implements GroupIncludeCustomRepository {
	public GroupIncludeCustomRepositoryImpl() {
		super(GroupInclude.class);
	}

	@Override
	public List<GroupInclude> findByGroupNumbers(List<Long> gno) {
		QGroupInclude groupInclude = QGroupInclude.groupInclude;
		QGroupEntity group = QGroupEntity.groupEntity;
		QMemberEntity member = QMemberEntity.memberEntity;
		JPQLQuery<GroupInclude> query = from(groupInclude);
		query.innerJoin(groupInclude.group,group).fetchJoin();
		query.innerJoin(groupInclude.member,member).fetchJoin();
		query.where(groupInclude.group.gno.in(gno)
					.and(
							groupInclude.whichStatus.eq(WhichStatus.LEADER)
							.or(groupInclude.whichStatus.eq(WhichStatus.FOLLOWER))
						));
		return query.fetch();
	}

	@Override
	public List<MemberManageVO> findMemberManageByGroupNumber(Long gno) {
		QGroupInclude groupInclude = QGroupInclude.groupInclude;
		JPQLQuery<Tuple> query = from(groupInclude).select(
													groupInclude.member.mno,
													groupInclude.member.name,
													groupInclude.attendance,
													groupInclude.absent,
													groupInclude.late,
													groupInclude.deposit,
													groupInclude.fine,
													groupInclude.reward
													);
		query.where(groupInclude.group.gno.eq(gno).and(groupInclude.whichStatus.eq(WhichStatus.LEADER).or(groupInclude.whichStatus.eq(WhichStatus.FOLLOWER))));
		List<Tuple> tupleList = query.fetch();
		List<MemberManageVO> list = new ArrayList<>();
		for(Tuple t:tupleList) {
			list.add(MemberManageVO.builder()
									.mno(t.get(groupInclude.member.mno))
									.name(t.get(groupInclude.member.name))
									.attendance(t.get(groupInclude.attendance))
									.absent(t.get(groupInclude.absent))
									.late(t.get(groupInclude.late))
									.deposit(t.get(groupInclude.deposit))
									.fine(t.get(groupInclude.fine))
									.reward(t.get(groupInclude.reward))
									.build()
					);
		}
		return list;
	}

	@Override
	public List<GroupInclude> findByGroupNumber(Long gno) {
		QGroupInclude groupInclude = QGroupInclude.groupInclude;
		QGroupEntity group = QGroupEntity.groupEntity;
		QMemberEntity member = QMemberEntity.memberEntity;
		JPQLQuery<GroupInclude> query = from(groupInclude);
		query.innerJoin(groupInclude.group,group).fetchJoin();
		query.innerJoin(groupInclude.member,member).fetchJoin();
		query.where(groupInclude.group.gno.eq(gno)
					.and(
						groupInclude.whichStatus.eq(WhichStatus.LEADER)
						.or(groupInclude.whichStatus.eq(WhichStatus.FOLLOWER))
						));
		return query.fetch();
	}

	@Override
	public List<GroupInclude> findByGroupNumberAndWhichStatus(Long gno, WhichStatus whichStatus) {
		QGroupInclude groupInclude = QGroupInclude.groupInclude;
		JPQLQuery<Tuple> query = from(groupInclude).select(groupInclude.gino,groupInclude.member.name,groupInclude.member.role);
		query.where(groupInclude.group.gno.eq(gno).and(groupInclude.whichStatus.eq(whichStatus)));
		List<Tuple> tuple = query.fetch();
		List<GroupInclude> ret = new ArrayList<>();
		for(Tuple t:tuple) {
			ret.add(GroupInclude.builder()
							.gino(t.get(groupInclude.gino))
							.member(MemberEntity.builder().name(t.get(groupInclude.member.name)).role(t.get(groupInclude.member.role)).build())
							.build());
		}
		return ret;
	}

	@Override
	public void rejectAllGroup(Long gno) {
		QGroupInclude groupInclude = QGroupInclude.groupInclude;
		
		update(groupInclude).where(groupInclude.group.gno.eq(gno).and(groupInclude.whichStatus.eq(WhichStatus.WAITING)))
			.set(groupInclude.whichStatus, WhichStatus.REJECT)
			.execute();
		
	}
}
