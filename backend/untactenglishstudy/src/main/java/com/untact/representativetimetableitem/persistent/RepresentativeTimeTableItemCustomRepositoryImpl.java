package com.untact.representativetimetableitem.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.JPQLQuery;
import com.untact.representativetimetableitem.domain.QRepresentativeTimeTableItem;
import com.untact.representativetimetableitem.domain.RepresentativeTimeTableItem;

public class RepresentativeTimeTableItemCustomRepositoryImpl extends QuerydslRepositorySupport implements RepresentativeTimeTableItemCustomRepository {
	public RepresentativeTimeTableItemCustomRepositoryImpl() {
		super(RepresentativeTimeTableItem.class);
	}

	@Override
	public List<Long> findGroupNumberByDayAndStartTime(int day, int startHour, int startMinute) {
		QRepresentativeTimeTableItem i = QRepresentativeTimeTableItem.representativeTimeTableItem;
		JPQLQuery<Long> query = from(i).select(i.group.gno);
		query.where(i.day.eq(day).and(i.startHour.eq(startHour)).and(i.startMinute.eq(startMinute)));
		return query.fetch();
	}
}
