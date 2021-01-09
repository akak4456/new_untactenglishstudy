package com.untact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.member.domain.MemberEntity;
import com.untact.security.AuthenticationFacade;
import com.untact.service.vocabulary.VocabularyService;
import com.untact.vo.PageMaker;
import com.untact.vo.PageVO;
import com.untact.vo.VocabularyPageResponse;
import com.untact.vo.VocabularyPageVO;
import com.untact.vo.VocabularyRequest;
import com.untact.vocabulary.domain.Vocabulary;

@RestController
public class VocabularyController {
	@Autowired
	private VocabularyService vocabularyService;
	@GetMapping("/vocabulary/{groupid}")
	public ResponseEntity<VocabularyPageResponse> getPage(@PathVariable("groupid")Long gno,PageVO pageVO){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		VocabularyPageVO vo = vocabularyService.getListWithPagingAndGroupNumberAndMemberNumber(pageVO, gno,member.getMno());
		PageMaker<Vocabulary> pageMaker = new PageMaker<>(vo.getPage(),pageVO);
		return new ResponseEntity<>(new VocabularyPageResponse(pageMaker,vo.getDefaultVocaCount()),HttpStatus.OK);
	}
	@GetMapping("/vocabulary/{groupid}/{vocabularyid}")
	public ResponseEntity<PageMaker<EnglishDictionary>> getVocabularyItemPage(@PathVariable("groupid")Long gno,@PathVariable("vocabularyid")Long vno,PageVO pageVO){
		PageMaker<EnglishDictionary> maker = new PageMaker<>(vocabularyService.getVocabularyItemsWithPagingAndVocabularyNumber(pageVO, vno),pageVO);
		return new ResponseEntity<>(maker,HttpStatus.OK);
	}
	@PostMapping("/vocabulary/{groupid}")
	public ResponseEntity<String> addVoca(@PathVariable("groupid")Long gno,@RequestBody VocabularyRequest request){
		MemberEntity member = AuthenticationFacade.getMemberEntityFromAuthentication();
		vocabularyService.addVoca(gno, member, request.getTitle(), request.getContent());
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
}
