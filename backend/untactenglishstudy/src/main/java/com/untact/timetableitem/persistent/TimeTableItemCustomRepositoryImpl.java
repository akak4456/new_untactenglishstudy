package com.untact.timetableitem.persistent;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.untact.timetableitem.domain.TimeTableItem;

public class TimeTableItemCustomRepositoryImpl extends QuerydslRepositorySupport
		implements TimeTableItemCustomRepository {
	public TimeTableItemCustomRepositoryImpl() {
		super(TimeTableItem.class);
	}
}
