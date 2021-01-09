package com.untact.representativetimetableitem.persistent;

import java.util.List;

import org.springframework.data.repository.query.Param;

public interface RepresentativeTimeTableItemCustomRepository {
	public List<Long> findGroupNumberByDayAndStartTime(@Param("day")int day,@Param("startHour")int startHour, @Param("startMinute")int startMinute);
}
