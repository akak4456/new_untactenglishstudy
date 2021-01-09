package com.untact.service.quiz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.englishspelling.domain.EnglishSpellingDifficulty;
import com.untact.englishspelling.persistent.EnglishSpellingRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.vo.QuizResponse;
import com.untact.vo.QuizVO;
import com.untact.vocabularyitem.persistent.VocabularyItemRepository;
import com.untact.workbook.domain.Workbook;
import com.untact.workbook.domain.WorkbookKind;
import com.untact.workbook.persistent.WorkbookRepository;

@Service
public class QuizServiceImpl implements QuizService {
	public static final int MINIMUM_VOCABULARY_ITEM_COUNT = 3;
	@Autowired
	private EnglishSpellingRepository englishSpellingRepo;
	@Autowired
	private VocabularyItemRepository vocabularyItemRepo;
	@Autowired
	private WorkbookRepository workbookRepo;
	@Override
	public List<Workbook> generateRandomQuiz(Long cnt) {
		return workbookRepo.findByRandom(cnt);
	}
	@Override
	public Optional<QuizResponse> generateQuiz(Long gno, MemberEntity member, Long vno, String kind, Long count,
			String difficulty) {
		// TODO Auto-generated method stub
		WorkbookKind retKind = WorkbookKind.valueOf(kind);
		EnglishSpellingDifficulty diff = EnglishSpellingDifficulty.valueOf(difficulty);
		Random random = new Random();
		Map<EnglishSpelling,List<Workbook>> cand = null;
		if(vno == 0L) {
			//사용자가 기본 단어장을 이용해 퀴즈를 만들기를 원하면
			if(diff == EnglishSpellingDifficulty.any) {
				cand = workbookRepo.findByKind(retKind).stream().collect(Collectors.groupingBy(Workbook::getEnglishSpelling));
			}else {
				cand = workbookRepo.findByKindAndDifficulty(retKind,diff).stream().collect(Collectors.groupingBy(Workbook::getEnglishSpelling));
			}
		}else {
			List<EnglishSpelling> words = vocabularyItemRepo.findEnglishSpellingByVocaburaryNumber(vno);
			if(words.size() < MINIMUM_VOCABULARY_ITEM_COUNT) {
				//단어장에 있는 단어의 개수가 너무 적으면
				return Optional.empty();
			}
			cand = workbookRepo.findBywordListAndKind(words, retKind).stream().collect(Collectors.groupingBy(Workbook::getEnglishSpelling));
		}
		List<EnglishSpelling> spellingList = new ArrayList<>(cand.keySet());
		if(spellingList.size() < count) {
			return Optional.empty();
		}
		Set<Integer> problemIdx = new HashSet<>();
		while(problemIdx.size() < count) {
			problemIdx.add(random.nextInt(cand.keySet().size()));
		}
		List<QuizVO> vo = new ArrayList<>();
		for(Integer idx:problemIdx) {
			List<Workbook> wList = cand.get(spellingList.get(idx));
			Workbook w = wList.get(random.nextInt(wList.size()));
			vo.add(new QuizVO(w.getQuestion(),w.getAns1(),w.getAns2(),w.getAns3(),w.getAns4(),w.getEnglishSpelling().getSpelling()));
		}
		return Optional.of(new QuizResponse(vo));
	}
}
