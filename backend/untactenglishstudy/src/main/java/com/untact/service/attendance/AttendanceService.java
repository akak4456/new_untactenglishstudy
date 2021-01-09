package com.untact.service.attendance;

import org.springframework.data.domain.Page;

import com.untact.attendance.domain.Attendance;
import com.untact.controller.TryAttendanceResult;
import com.untact.vo.PageVO;

public interface AttendanceService {
	/*
	 출석 체크 알고리즘
	 1. 매 분마다 insertAbsentPeriodically를 실행함
	 2. 만약에 insertAbsentPeriodically를 실행하는 중에 현재 시간과 대표 시간표의 시작 시간이 일치하는 게 있다면 거기에 있는 팀원들
	 모두 결석 처리 함
	 3. 출석 체크를 누른 사람에 한해서 출석 또는 지각으로 상태 변경
	 */
	public void insertAbsentPeriodically(int day, int startHour, int startMinute);
	
	public TryAttendanceResult attendanceCheck(Long gno, Long mno);
	
	public Long getRemainTime(Long gno, Long mno);
	
	public Page<Attendance> getList(PageVO pageVO,Long gno, Long mno);
}
