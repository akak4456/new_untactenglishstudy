package com.untact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.untact.attendance.domain.Attendance;
import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.attendance.AttendanceService;
import com.untact.vo.PageMaker;
import com.untact.vo.PageVO;

@RestController
public class AttendanceController {
	@Autowired
	private AttendanceService attendanceService;
	@PutMapping("/attendance/{groupid}")
	public ResponseEntity<String> attendanceCheck(@PathVariable("groupid")Long gno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		return new ResponseEntity<>(attendanceService.attendanceCheck(gno, member.getMno()).toString(),HttpStatus.OK);
	}
	@GetMapping("/attendance/time/{groupid}")
	public ResponseEntity<Long> remainTime(@PathVariable("groupid")Long gno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		return new ResponseEntity<>(attendanceService.getRemainTime(gno, member.getMno()),HttpStatus.OK);
	}
	@GetMapping("/attendance/{groupid}")
	public ResponseEntity<PageMaker<Attendance>> getList(@PathVariable("groupid")Long gno,PageVO pageVO){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		return new ResponseEntity<>(new PageMaker<Attendance>(attendanceService.getList(pageVO, gno, member.getMno()),pageVO),HttpStatus.OK);
	}
}
