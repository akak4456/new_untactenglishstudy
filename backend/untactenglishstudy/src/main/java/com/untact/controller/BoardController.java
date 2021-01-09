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

import com.untact.board.domain.Board;
import com.untact.board.domain.BoardKind;
import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.board.BoardService;
import com.untact.vo.PageMaker;
import com.untact.vo.PageVO;

import lombok.extern.java.Log;

@RestController
@Log
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/board/{groupid}")
	public ResponseEntity<PageMaker<Board>> getBoards(PageVO pageVO,@PathVariable("groupid")Long gno) {
		return new ResponseEntity<>(new PageMaker<Board>(boardService.getListWithPagingAndGroupNumber(pageVO, gno),pageVO),HttpStatus.OK);
	}
	@GetMapping("/board/kind/{groupid}/{kind}")
	public ResponseEntity<PageMaker<Board>> getBoardsWithKind(PageVO pageVO,@PathVariable("groupid") Long gno,@PathVariable("kind")BoardKind kind){
		return new ResponseEntity<>(new PageMaker<Board>(boardService.getListWithPagingAndGroupNumberAndKind(pageVO, gno, kind),pageVO),HttpStatus.OK);
	}
	@GetMapping("/board/{groupid}/{boardid}")
	public ResponseEntity<Board> getBoard(@PathVariable("groupid")Long gno,@PathVariable("boardid")Long bno){
		return new ResponseEntity<>(boardService.getOne(bno),HttpStatus.OK);
	}
	
	@PostMapping("/board/{groupid}")
	public ResponseEntity<String> addBoard(@RequestBody Board board,@PathVariable("groupid")Long gno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		boardService.addBoard(board,gno,member);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	
	@PutMapping("/board/{boardid}/{memberid}")
	public ResponseEntity<String> modifyBoard(@RequestBody Board targetBoard,@PathVariable("boardid")Long bno,@PathVariable("memberid")Long mno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(member.getMno() != mno) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		boardService.modifyBoard(targetBoard, bno);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	@DeleteMapping("/board/{boardid}/{memberid}")
	public ResponseEntity<String> deleteBoard(@PathVariable("boardid")Long bno,@PathVariable("memberid")Long mno){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		if(member.getMno() != mno) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		boardService.deleteBoard(bno);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
	
}
