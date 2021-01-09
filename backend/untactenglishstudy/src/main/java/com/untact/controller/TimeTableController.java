package com.untact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.untact.exception.NotGroupLeaderException;
import com.untact.exception.NotIncludeGroupException;
import com.untact.exception.TimeTableNotCorrectException;
import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.timetable.TimeTableService;
import com.untact.timetable.domain.TimeTable;
import com.untact.vo.PageMaker;
import com.untact.vo.PageVO;
import com.untact.vo.RepresentativeTimeTableVO;
import com.untact.vo.TimeTableVO;

@RestController
public class TimeTableController {
	/*
    시간표가 맞는지, 겹치는 스티커들(일정)은 없는지 확인
     */
	@Autowired
	private TimeTableService timeTableService;
	@GetMapping("/timetable/{groupid}")
	public ResponseEntity<PageMaker<TimeTable>> getTimeTableList(@PathVariable("groupid")Long gno,PageVO pageVO){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		return new ResponseEntity<>(new PageMaker<TimeTable>(
				timeTableService.getListWithPagingAndGroupNumberAndMemberNumber(pageVO, gno, member.getMno()),
				pageVO
				),HttpStatus.OK);
	}
	@GetMapping("/timetable/{groupid}/{timetableid}")
	public ResponseEntity<TimeTableVO> getOne(@PathVariable("groupid")Long gno,@PathVariable("timetableid")Long tno){
		return new ResponseEntity<>(timeTableService.getOne(tno),HttpStatus.OK);
	}
	@PostMapping("/timetable/{groupid}")
	public ResponseEntity<String> addTimeTable(@PathVariable("groupid")Long gno,@RequestBody TimeTableVO timeTableVO){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		try {
			timeTableService.addTimeTable(gno,member,timeTableVO.getTimeTable(), timeTableVO.getTimeTableItem());
			return new ResponseEntity<String>("success",HttpStatus.OK);
		} catch (NotIncludeGroupException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}catch (TimeTableNotCorrectException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping("/timetable/{groupid}/{timetableid}")
	public ResponseEntity<String> modifyTimeTable(@PathVariable("groupid")Long gno,@PathVariable("timetableid")Long tno,@RequestBody TimeTableVO timeTableVO){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		try {
			timeTableService.modifyTimeTable(gno, member.getMno(), tno, timeTableVO.getTimeTable(), timeTableVO.getTimeTableItem());
			return new ResponseEntity<String>("success",HttpStatus.OK);
		}catch (NotIncludeGroupException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (TimeTableNotCorrectException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/timetable/{groupid}/{timetableid}")
	public ResponseEntity<String> deleteTimeTable(@PathVariable("groupid")Long gno,@PathVariable("timetableid")Long tno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		try {
			timeTableService.deleteTimeTable(gno, member.getMno(), tno);
			return new ResponseEntity<String>("success",HttpStatus.OK);
		}catch (NotIncludeGroupException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/timetable/represent/{groupid}")
	public ResponseEntity<RepresentativeTimeTableVO> getRepresentativeOne(@PathVariable("groupid")Long gno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		try {
			return new ResponseEntity<>(timeTableService.getRepresentativeOne(gno, member),HttpStatus.OK);
		}catch(NotGroupLeaderException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping("/timetable/represent/{groupid}")
	public ResponseEntity<String> modifyTimeTable(@PathVariable("groupid")Long gno,@RequestBody RepresentativeTimeTableVO timeTableVO){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		try {
			timeTableService.modifyRepresentativeTimeTable(gno, member, timeTableVO.getTitle(), timeTableVO.getTimeTableItem());
			return new ResponseEntity<>("success",HttpStatus.OK);
		}catch (NotGroupLeaderException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}catch (TimeTableNotCorrectException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} 
	}
	@PutMapping("/timetable/toggle/{groupid}/{timetableid}")
	public ResponseEntity<TimeTableVO> toggleAlarm(@PathVariable("groupid")Long gno,@PathVariable("timetableid")Long tno){
		return new ResponseEntity<>(timeTableService.toggleAlarm(tno),HttpStatus.OK);
	}
}
