package com.untact.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.untact.exception.NoMatchMemberInformationException;
import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.security.JwtTokenProvider;
import com.untact.service.email.EmailService;
import com.untact.service.member.MemberService;
import com.untact.vo.EmailCheckVO;
import com.untact.vo.LoginResultVO;
import com.untact.vo.MemberVO;

import lombok.extern.java.Log;

@RestController
@Log
public class MemberController {
	@Autowired
	private EmailService emailService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private MemberService memberService;
	@Value("${rootaddress}")
	private String rootAddress;
	@PostMapping("/member/sign_up")
	public ResponseEntity<String> addMember(@Valid @RequestBody MemberVO memberVO,BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			for (Object object : bindingResult.getAllErrors()) {
				if (object instanceof FieldError) {
					FieldError fieldError = (FieldError) object;
					return new ResponseEntity<>(fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST);
				}
			}
		}
		if(memberService.isDuplicateEmail(memberVO.getEmail())) {
			return new ResponseEntity<>("emailduplicate",HttpStatus.BAD_REQUEST);
		}
		Long mno = memberService.addMember(memberVO);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	@PostMapping("/member/login")
	public ResponseEntity<LoginResultVO> login(@RequestBody MemberVO memberVO){
		try {
			MemberEntity member = memberService.login(memberVO);
			LoginResultVO ret = new LoginResultVO(jwtTokenProvider.createToken(member.getEmail(), member.getRole()),member.copy());
			return new ResponseEntity<>(ret,HttpStatus.OK);
		}catch(NoMatchMemberInformationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/member/emailcheck")
	public ResponseEntity<String> emailCheck(@RequestBody EmailCheckVO emailCheckVO){
		if(memberService.isDuplicateEmail(emailCheckVO.getEmail())) {
			//이미 이메일이 존재한다면
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<>("success",HttpStatus.OK);
		}
	}
	@PostMapping("/member/sendcheckcode")
	public ResponseEntity<String> sendCheckCode(){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		String code = generateCode();
		sendEmailCode(member.getEmail(),code);
		memberService.updateAuthKey(member.getEmail(), code);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	@PostMapping("/member/emailcheck/{checkcode}")
	public ResponseEntity<String> emailCheckCode(@PathVariable("checkcode")String checkCode){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(checkCode.equals(member.getAuthKey())) {
			memberService.updateIsEmailCheckToTrue(member.getEmail());
			return new ResponseEntity<>("success",HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/member")
	public ResponseEntity<MemberEntity> getMember(){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		return new ResponseEntity<>(member.copy(),HttpStatus.OK);
	}
	private String generateCode() {
		return RandomStringUtils.randomNumeric(6);
	}
	private void sendEmailCode(String to,String code) {
		StringBuffer content = new StringBuffer();
		content.append("<strong>"+code+"</strong>");
		content.append(" 를 입력해주세요");
		emailService.sendMail( to,"이메일 인증을 완료해주세요!" , content.toString());
	}
	
	@PutMapping("/member/pay/{amount}")
	public ResponseEntity<String> pay(@PathVariable("amount")Long amount){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		memberService.pay(member,amount);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	
	@PutMapping("/member/refund/{amount}")
	public ResponseEntity<String> refund(@PathVariable("amount")Long amount){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(memberService.refund(member,amount))
			return new ResponseEntity<>("success",HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
