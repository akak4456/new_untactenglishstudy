package com.untact.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.member.MemberService;
import com.untact.vo.ChangeMemberInfoVO;
import com.untact.vo.ChangeMemberPasswordVO;

import lombok.extern.java.Log;

@RestController
@Log
public class InfoController {
	@Autowired
	private MemberService memberService;
	@PutMapping("/member/info")
	public ResponseEntity<String> changeInfo(@Valid @RequestBody ChangeMemberInfoVO memberVO,BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {	
					FieldError fieldError = (FieldError) object;
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(!member.getEmail().equals(memberVO.getEmail())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		memberService.changeInfo(member, memberVO);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	@PutMapping("/member/info/pw")
	public ResponseEntity<String> changePw(@Valid @RequestBody ChangeMemberPasswordVO memberVO,BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {	
					FieldError fieldError = (FieldError) object;
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(!member.getEmail().equals(memberVO.getEmail())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(memberService.changePassword(member, memberVO)) {
			return new ResponseEntity("success",HttpStatus.OK);
		}else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/member/info")
	public ResponseEntity<String> deleteInfo(){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		memberService.deleteInfo(member.getMno());
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
}
