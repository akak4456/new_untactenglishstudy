package com.untact.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.quiz.QuizService;
import com.untact.vo.QuizResponse;

@RestController
public class QuizController {
	@Autowired
	private QuizService quizService;
	@PostMapping("/quiz/{groupid}/{vocabularyid}/{kind}/{count}/{difficulty}")
	public ResponseEntity<QuizResponse> makeQuizWithDifficulty(
			@PathVariable("groupid")Long gno,
			@PathVariable("vocabularyid")Long vno,
			@PathVariable("kind")String kind,
			@PathVariable("count")Long count,
			@PathVariable("difficulty")String difficulty
			){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		Optional<QuizResponse> res = quizService.generateQuiz(gno, member, vno, kind, count,difficulty);
		if(res.isPresent()) {
			return new ResponseEntity<>(res.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
