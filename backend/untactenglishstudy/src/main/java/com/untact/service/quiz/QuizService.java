package com.untact.service.quiz;

import java.util.List;
import java.util.Optional;

import com.untact.member.domain.MemberEntity;
import com.untact.vo.QuizResponse;
import com.untact.workbook.domain.Workbook;

public interface QuizService {
	public Optional<QuizResponse> generateQuiz(Long gno, MemberEntity member, Long vno, String kind, Long count,String difficulty);
	public List<Workbook> generateRandomQuiz(Long cnt);
}
