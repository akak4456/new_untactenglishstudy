package com.untact.cron;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.untact.service.attendance.AttendanceService;
import com.untact.service.group.GroupService;

import lombok.extern.java.Log;

@Component
@Log
public class Scheduler {
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private GroupService groupService;
	@Scheduled(cron = "0 * * * * *")
	public void everyMinuteRun() {
		//매 분마다 돌아가는 알고리즘
		//즉 0초가 될때마다 실행됨
		LocalDateTime curTime = LocalDateTime.now();
		int day = curTime.getDayOfWeek().getValue()-1;//Mon-1 Sun-7 이므로 거기에다 1을 빼야 함
		int hour = curTime.getHour();
		int minute = curTime.getMinute();
		attendanceService.insertAbsentPeriodically(day, hour, minute);
	}
	@Scheduled(cron = "0 0 0 * * *")
	public void everyOnTimeRun() {
		//매일 오전 12시에 돌아가는 알고리즘
		LocalDateTime now = LocalDateTime.now();

		groupService.dismissGroupAuto(now.with(LocalTime.MIDNIGHT));
	}
}
