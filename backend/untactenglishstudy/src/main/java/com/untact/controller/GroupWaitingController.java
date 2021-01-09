package com.untact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.groupinclude.GroupIncludeService;
import com.untact.vo.GroupWaitingVO;

import lombok.extern.java.Log;

@RestController
@Log
public class GroupWaitingController {
	@Autowired
	private GroupIncludeService groupIncludeService;
	
	@PostMapping("/waiting/{groupid}")
	public ResponseEntity<String> requestJoin(@PathVariable("groupid")Long gno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		GroupWaitingVO groupWaitingVO = new GroupWaitingVO(gno,member.getMno());
		if(groupIncludeService.requestJoin(groupWaitingVO)) {
			return new ResponseEntity<>("success",HttpStatus.OK);
		}else {
			//사람이 다 찾을 경우에
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/waiting/{groupid}/{gino}/accept")
	public ResponseEntity<String> acceptJoin(@PathVariable("groupid")Long gno,@PathVariable("gino")Long gino){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		groupIncludeService.acceptJoin(gno,gino,member);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	
	@PutMapping("/waiting/{groupid}/{gino}/reject")
	public ResponseEntity<String> rejectJoin(@PathVariable("groupid")Long gno,@PathVariable("gino")Long gino){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		groupIncludeService.rejectJoin(gno,gino,member);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
}
