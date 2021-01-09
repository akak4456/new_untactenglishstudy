package com.untact.timetable.persistent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.untact.timetable.domain.QTimeTable;
import com.untact.timetable.domain.TimeTable;

public class TimeTableCustomRepositoryImpl extends QuerydslRepositorySupport implements TimeTableCustomRepository {
	public TimeTableCustomRepositoryImpl() {
		super(TimeTable.class);
	}

	@Override
	public Page<TimeTable> getPageWithGroupNumberAndMemberNumber(Pageable pageable, Long gno, Long mno) {
		QTimeTable timeTable = QTimeTable.timeTable;
		JPQLQuery<Tuple> query = from(timeTable).select(timeTable.tno,timeTable.title,timeTable.isalarm);
		query.where(timeTable.group.gno.eq(gno)
					.and(timeTable.member.mno.eq(mno)));
		Long totalCount = query.fetchCount();
		List<Tuple> list = getQuerydsl().applyPagination(pageable, query).fetch();
		List<TimeTable> timeTableList = new ArrayList<>();
		for(Tuple t:list) {
			timeTableList.add(TimeTable.builder()
					.tno(t.get(timeTable.tno))
					.title(t.get(timeTable.title))
					.isalarm(t.get(timeTable.isalarm))
					.build());
		}
		return new PageImpl<TimeTable>(timeTableList,pageable,totalCount);
	}

}
