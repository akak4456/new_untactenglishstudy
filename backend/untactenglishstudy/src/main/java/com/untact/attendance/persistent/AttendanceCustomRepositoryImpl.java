package com.untact.attendance.persistent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.untact.attendance.domain.Attendance;
import com.untact.attendance.domain.QAttendance;

public class AttendanceCustomRepositoryImpl extends QuerydslRepositorySupport implements AttendanceCustomRepository {
	public AttendanceCustomRepositoryImpl() {
		super(Attendance.class);
	}

	@Override
	public Attendance findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(Long gno,Long mno,LocalDateTime startTime,LocalDateTime endTime) {
		QAttendance attendance = QAttendance.attendance;
		JPQLQuery<Attendance> query = from(attendance);
		query.where(
				attendance.group.gno.eq(gno)
				.and(attendance.member.mno.eq(mno))
				.and(attendance.regdate.between(startTime,endTime))
				);
		return query.fetchOne();
	}

	@Override
	public Page<Attendance> getPageWithGroupNumberAndMemberNumber(Pageable pageable,Long gno, Long mno) {
		QAttendance attendance = QAttendance.attendance;
		JPQLQuery<Attendance> query = from(attendance);
		query.where(attendance.group.gno.eq(gno).and(attendance.member.mno.eq(mno)));
		Long totalCount = query.fetchCount();
		List<Attendance> list = getQuerydsl().applyPagination(pageable, query).fetch();
		return new PageImpl<Attendance>(list,pageable,totalCount);
	}

	@Override
	public List<Attendance> findByGroupNumberAndLocalDate(Long gno, LocalDate time) {
		QAttendance attendance = QAttendance.attendance;
		JPQLQuery<Tuple> query = from(attendance).select(
										attendance.member,
										attendance.ano,
										attendance.status
									);
		query.where(attendance.group.gno.eq(gno)
					.and(attendance.regdate.between(time.atStartOfDay(), time.plusDays(1L).atStartOfDay())));
		List<Tuple> tuple = query.fetch();
		List<Attendance> response = new ArrayList<>();
		for(Tuple t:tuple) {
			response.add(Attendance.builder()
									.member(t.get(attendance.member))
									.ano(t.get(attendance.ano))
									.status(t.get(attendance.status))
									.build());
		}
		return response;
	}

}
