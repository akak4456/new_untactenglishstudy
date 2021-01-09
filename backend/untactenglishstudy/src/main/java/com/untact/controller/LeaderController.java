package com.untact.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.untact.attendance.vo.AttendanceResponse;
import com.untact.exception.NotGroupLeaderException;
import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.leader.LeaderService;
import com.untact.vo.MemberManageResponse;
import com.untact.vo.WaitingResponse;

import lombok.extern.java.Log;

@RestController
@Log
public class LeaderController {
	@Autowired
	private LeaderService leaderService;
	
	@GetMapping("/leader/{groupid}")
	public ResponseEntity<MemberManageResponse> getList(@PathVariable("groupid")Long gno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		try {
			MemberManageResponse response = new MemberManageResponse();
			response.setMembers(leaderService.getListWithGroupNumber(gno, member));
			return new ResponseEntity<>(response,HttpStatus.OK);
		}catch(NotGroupLeaderException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/leader/reward/{groupid}/{targetMno}/{newAmount}")
	public ResponseEntity<String> changeReward(
			@PathVariable("groupid")Long gno,
			@PathVariable("targetMno")Long targetMno,
			@PathVariable("newAmount")Long newAmount
	){
		MemberEntity leader = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(leaderService.changeReward(gno, leader, targetMno, newAmount)) {
			return new ResponseEntity<>("success",HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping("/leader/fine/{groupid}/{targetMno}/{newAmount}")
	public ResponseEntity<String> changeFine(
			@PathVariable("groupid")Long gno,
			@PathVariable("targetMno")Long targetMno,
			@PathVariable("newAmount")Long newAmount
	){
		MemberEntity leader = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(leaderService.changeFine(gno, leader, targetMno, newAmount)) {
			return new ResponseEntity<>("success",HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/leader/eject/{groupid}/{targetMno}")
	public ResponseEntity<String> eject(
			@PathVariable("groupid")Long gno,
			@PathVariable("targetMno")Long targetMno
	){
		MemberEntity leader = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(leaderService.forceExit(gno, leader, targetMno)) {
			return new ResponseEntity<>("success",HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/leader/attendance/{groupid}/{curTime}")
	public ResponseEntity<AttendanceResponse> getAttendanceList(@PathVariable("groupid")Long gno,@PathVariable("curTime")String curTime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate time = LocalDate.parse(curTime,formatter);
		return new ResponseEntity<>(new AttendanceResponse(leaderService.getAttendanceListWithGroupNumberAndLocalDate(gno, time)),HttpStatus.OK);
	}
	
	@PutMapping("/leader/attendance/{groupid}/{targetMno}/{attendanceid}/{oldStatus}/{newStatus}")
	public ResponseEntity<String> changeAttendance(
			@PathVariable("groupid")Long gno,
			@PathVariable("targetMno")Long targetMno,
			@PathVariable("attendanceid")Long ano,
			@PathVariable("oldStatus")String oldStatus,
			@PathVariable("newStatus")String newStatus){
		MemberEntity leader = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(leaderService.changeAttendance(gno, leader, targetMno,ano, oldStatus, newStatus)) {
			return new ResponseEntity<>("success",HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/leader/waiting/{groupid}")
	public ResponseEntity<WaitingResponse> getWaitingList(@PathVariable("groupid")Long gno){
		return new ResponseEntity<>(new WaitingResponse(leaderService.getWaitingList(gno)),HttpStatus.OK);
	}
	
	@PutMapping("/leader/rejectall/{groupid}")
	public ResponseEntity<String> rejectAll(@PathVariable("groupid")Long gno){
		MemberEntity leader = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(leaderService.rejectAll(gno, leader)) {
			return new ResponseEntity<>("success",HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
