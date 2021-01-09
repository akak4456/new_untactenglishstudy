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

import com.untact.member.domain.MemberEntity;
import com.untact.reply.domain.Reply;
import com.untact.security.AuthenticationFacade;
import com.untact.service.reply.ReplyService;
import com.untact.vo.PageMaker;
import com.untact.vo.PageVO;

import lombok.extern.java.Log;

@RestController
@Log
public class ReplyController {
	@Autowired
	private ReplyService replyService;
	
	@GetMapping("/reply/{groupid}/{boardid}")
	public ResponseEntity<PageMaker<Reply>> getReplies(
			PageVO pageVO,
			@PathVariable("groupid")Long gno,
			@PathVariable("boardid")Long bno){
		return new ResponseEntity<>(new PageMaker<Reply>(replyService.getListWithPagingAndBoardNumber(pageVO, bno),pageVO),HttpStatus.OK);
	}
	@PostMapping("/reply/{groupid}/{boardid}")
	public ResponseEntity<String> addReply(@RequestBody Reply reply,@PathVariable("groupid")Long gno,@PathVariable("boardid")Long bno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		replyService.addReply(reply, gno, bno,member);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	@PutMapping("/reply/{replyid}/{memberid}")
	public ResponseEntity<String> modifyReply(@RequestBody Reply targetReply,@PathVariable("replyid")Long rno,@PathVariable("memberid")Long mno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(member.getMno() != mno) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		replyService.modifyReply(targetReply, rno);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	@DeleteMapping("/reply/{replyid}/{memberid}")
	public ResponseEntity<String> deleteReply(@PathVariable("replyid")Long rno,@PathVariable("memberid")Long mno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(member.getMno() != mno) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		replyService.deleteReply(rno);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
}
