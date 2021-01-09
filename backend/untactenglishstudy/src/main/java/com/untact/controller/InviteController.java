package com.untact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.invite.InviteService;

@RestController
public class InviteController {
	@Autowired
	private InviteService inviteService;
	@PutMapping("/invite/accept/{groupid}/{inviteCode}")
	public ResponseEntity<String> inviteAccept(@PathVariable("groupid")Long gno,@PathVariable("inviteCode")String inviteCode){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(inviteService.inviteAccept(gno, member, inviteCode))
			return new ResponseEntity<>("success",HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
