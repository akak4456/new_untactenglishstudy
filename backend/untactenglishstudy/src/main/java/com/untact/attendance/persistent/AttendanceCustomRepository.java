package com.untact.attendance.persistent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.untact.attendance.domain.Attendance;
import com.untact.attendance.vo.AttendanceVO;

public interface AttendanceCustomRepository {
	public Attendance findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(Long gno,Long mno,LocalDateTime startTime,LocalDateTime endTime);
	
	public Page<Attendance> getPageWithGroupNumberAndMemberNumber(Pageable pageable,Long gno,Long mno);
	
	public List<Attendance> findByGroupNumberAndLocalDate(Long gno,LocalDate time);
}
