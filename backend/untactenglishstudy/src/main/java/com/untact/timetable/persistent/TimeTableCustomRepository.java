package com.untact.timetable.persistent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.untact.timetable.domain.TimeTable;

public interface TimeTableCustomRepository {
	public Page<TimeTable> getPageWithGroupNumberAndMemberNumber(Pageable pageable,Long gno,Long mno);
}
