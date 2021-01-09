package com.untact.service.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.untact.antonym.domain.Antonym;
import com.untact.antonym.persistent.AntonymRepository;
import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.englishdictionary.persistent.EnglishDictionaryRepository;
import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.englishspelling.persistent.EnglishSpellingRepository;
import com.untact.phrase.domain.Phrase;
import com.untact.phrase.persistent.PhraseRepository;
import com.untact.service.crawler.CrawlerService;
import com.untact.thesaurus.domain.Thesaurus;
import com.untact.thesaurus.persistent.ThesaurusRepository;
import com.untact.workbook.domain.Workbook;
import com.untact.workbook.domain.WorkbookKind;
import com.untact.workbook.persistent.WorkbookRepository;

import lombok.extern.java.Log;

@Service
@Log
public class AdminServiceImpl implements AdminService {
	@Autowired
	private EnglishSpellingRepository englishSpellingRepo;
	@Autowired
	private EnglishDictionaryRepository englishDictionaryRepo;
	@Autowired
	private WorkbookRepository workbookRepo;
	@Autowired
	private ThesaurusRepository thesaurusRepo;
	@Autowired
	private AntonymRepository antonymRepo;
	@Autowired
	private PhraseRepository phraseRepo;
	@Autowired
	private CrawlerService crawlerService;

	@Transactional
	@Override
	public boolean setup() {
		if (englishSpellingRepo.count() == 0L) {
			log.info("english_spelling table must be inserted");
			return false;// english spelling에 데이터가 우선 들어가야만 한다
		}
		if (englishDictionaryRepo.count() == 0L) {
			log.info("english_dictionary table must be inserted");
			return false;
		}
		if (thesaurusRepo.count() == 0L) {
			log.info("thesaurus table must be inserted");
			return false;
		}
		if (antonymRepo.count() == 0L) {
			log.info("antonym table must be inserted");
			return false;
		}
		if (phraseRepo.count() == 0L) {
			log.info("phrase table must be inserted");
			return false;
		}
		workbookRepo.deleteAllInBatch();
		List<EnglishSpelling> spellingList = englishSpellingRepo.findAll();
		Map<EnglishSpelling, List<EnglishDictionary>> dic = englishDictionaryRepo.findAll().stream()
				.filter(d -> !d.getMeaning().equals("NOTFOUND"))
				.collect(Collectors.groupingBy(EnglishDictionary::getEnglishSpelling));// 사전 저장
		Map<EnglishSpelling, List<Thesaurus>> thesarus = thesaurusRepo.findAll().stream()
				.collect(Collectors.groupingBy(Thesaurus::getEnglishSpelling));

		Map<EnglishSpelling, List<Antonym>> antonym = antonymRepo.findAll().stream()
				.collect(Collectors.groupingBy(Antonym::getEnglishSpelling));
		List<Phrase> phraseList = phraseRepo.findAll();
		List<Workbook> workbooks = new ArrayList<>();
		// 영어-뜻, 뜻-영어 문제 생성
		Random random = new Random();
		for (EnglishSpelling spell : dic.keySet()) {
			for (int step = 0; step < 5; step++) {
				Set<Integer> wrongAnswerIdx = new HashSet<>();
				List<Thesaurus> li = thesarus.get(spell);
				List<EnglishDictionary> rightDic = dic.get(spell);
				String rightMean = rightDic.get(random.nextInt(rightDic.size())).getMeaning();
				while (wrongAnswerIdx.size() < 3) {
					int idx = random.nextInt(spellingList.size());
					String targetSpell = spellingList.get(idx).getSpelling();
					if (targetSpell.equals(spell.getSpelling())) {
						// 오답으로 뽑은 스펠링이 문제로 만들려고 하는 스펠링과 같다면
						continue;
					}
					boolean canDo = true;
					if (li != null) {
						for (int i = 0; i < li.size(); i++) {
							if (targetSpell.equals(li.get(i).getWordto())) {
								canDo = false;
								break;
							}
						}
					}
					if (!canDo) {
						// 오답으로 뽑은 스펠링이 문제를 만들려고 하는 것의 유사어의 스펠링과 같다면
						continue;
					}
					wrongAnswerIdx.add(idx);
				}
				List<EnglishSpelling> wrongSpells = new ArrayList<>();
				List<String> wrongMeaning = new ArrayList<>();
				for (Integer idx : wrongAnswerIdx) {
					EnglishSpelling wrongSpell = spellingList.get(idx);
					wrongSpells.add(wrongSpell);
					List<EnglishDictionary> wrongDic = dic.get(wrongSpell);
					wrongMeaning.add(wrongDic.get(random.nextInt(wrongDic.size())).getMeaning());
				}
				workbooks.add(Workbook.builder().englishSpelling(spell).question(spell.getSpelling())
						.kind(WorkbookKind.spelltomean).ans1(rightMean).ans2(wrongMeaning.get(0))
						.ans3(wrongMeaning.get(1)).ans4(wrongMeaning.get(2)).build());

				workbooks.add(Workbook.builder().englishSpelling(spell).question(rightMean)
						.kind(WorkbookKind.meantospell).ans1(spell.getSpelling()).ans2(wrongSpells.get(0).getSpelling())
						.ans3(wrongSpells.get(1).getSpelling()).ans4(wrongSpells.get(2).getSpelling()).build());
			}
		}
		// 유의어 문제 생성
		for (EnglishSpelling spell : dic.keySet()) {
			List<Thesaurus> thList = thesarus.get(spell);
			if (thList != null) {
				Collections.shuffle(thList);
				if (thList.size() > 5) {
					thList = thList.subList(0, 5);
				}
				// 최대 5문제만 생성함
				for (Thesaurus th : thList) {
					String rightAnswer = th.getWordto();
					if (rightAnswer.equals(spell.getSpelling())) {
						continue;
					}
					Set<Integer> wrongIdx = new HashSet<>();
					while (wrongIdx.size() < 3) {
						int idx = random.nextInt(spellingList.size());
						String targetSpell = spellingList.get(idx).getSpelling();
						if (spell.getSpelling().equals(targetSpell)) {
							continue;
						}
						boolean canDo = true;
						for (int i = 0; i < thList.size(); i++) {
							if (targetSpell.equals(thList.get(i).getWordto())) {
								canDo = false;
								break;
							}
						}
						if (!canDo) {
							continue;
						}
						wrongIdx.add(idx);
					}
					List<String> wrongAnswer = new ArrayList<>();
					for (Integer idx : wrongIdx) {
						wrongAnswer.add(spellingList.get(idx).getSpelling());
					}
					workbooks.add(Workbook.builder().englishSpelling(spell).question(spell.getSpelling())
							.kind(WorkbookKind.thesaurus).ans1(rightAnswer).ans2(wrongAnswer.get(0))
							.ans3(wrongAnswer.get(1)).ans4(wrongAnswer.get(2)).build());

				}
			}
		}
		// 반의어 문제 생성
		for (EnglishSpelling spell : dic.keySet()) {
			List<Antonym> anList = antonym.get(spell);
			if (anList != null) {
				Collections.shuffle(anList);
				if (anList.size() > 5) {
					anList = anList.subList(0, 5);
				}
				// 최대 5문제만 생성함
				for (Antonym an : anList) {
					String rightAnswer = an.getWordto();
					if (rightAnswer.equals(spell.getSpelling())) {
						continue;
					}
					Set<Integer> wrongIdx = new HashSet<>();
					while (wrongIdx.size() < 3) {
						int idx = random.nextInt(spellingList.size());
						String targetSpell = spellingList.get(idx).getSpelling();
						if (spell.getSpelling().equals(targetSpell)) {
							continue;
						}
						boolean canDo = true;
						for (int i = 0; i < anList.size(); i++) {
							if (targetSpell.equals(anList.get(i).getWordto())) {
								canDo = false;
								break;
							}
						}
						if (!canDo) {
							continue;
						}
						wrongIdx.add(idx);
					}
					List<String> wrongAnswer = new ArrayList<>();
					for (Integer idx : wrongIdx) {
						wrongAnswer.add(spellingList.get(idx).getSpelling());
					}
					workbooks.add(Workbook.builder().englishSpelling(spell).question(spell.getSpelling())
							.kind(WorkbookKind.antonym).ans1(rightAnswer).ans2(wrongAnswer.get(0))
							.ans3(wrongAnswer.get(1)).ans4(wrongAnswer.get(2)).build());

				}
			}
		}
		// 빈칸 유형 문제 생성
		for (EnglishSpelling spell : dic.keySet()) {
			for (EnglishDictionary d : dic.get(spell)) {
				workbooks.add(Workbook.builder().englishSpelling(spell)
						.question(d.getPartOfSpeech() + "\n" + d.getMeaning()).kind(WorkbookKind.blank)
						.ans1(spell.getSpelling()).ans2("none").ans3("none").ans4("none").build());
			}
		}

		// 구동사(phrase verb) 문제 생성
		/*
		 * 여기에서 phrase는 englishSpelling이 없는 것을 확인할수 있는데 이것은 english_spelling이 phrase에 있는
		 * verb를 담지 않을수도 있기 때문이다.
		 */

		Set<String> allPrep = new HashSet<>();// phrase 테이블에 있는 모든 전치사들
		// 문제의 정 오답을 만들때 쓰인다.
		for (int i = 0; i < phraseList.size(); i++) {
			allPrep.add(phraseList.get(i).getPrep());
		}
		List<String> allPrepToList = new ArrayList<>(allPrep);
		for (Phrase phrase : phraseList) {
			String problem = phrase.getEnglishSpelling().getSpelling() + " (__)\n" + phrase.getMeaning();
			String rightAnswer = phrase.getPrep();
			Set<Integer> wrongAnswerPos = new HashSet<>();// allPrep중에서 오답으로 뽑을 것들의 인덱스
			while (wrongAnswerPos.size() < 3) {
				int idx = random.nextInt(allPrepToList.size());
				String cand = allPrepToList.get(idx);
				if (cand.equals(rightAnswer)) {
					continue;
				}
				wrongAnswerPos.add(idx);
			}
			List<String> wrongAnswerList = new ArrayList<>();
			for (Integer idx : wrongAnswerPos) {
				wrongAnswerList.add(allPrepToList.get(idx));
			}
			workbooks.add(Workbook.builder().englishSpelling(phrase.getEnglishSpelling()).question(problem)
					.kind(WorkbookKind.phrase).ans1(rightAnswer).ans2(wrongAnswerList.get(0))
					.ans3(wrongAnswerList.get(1)).ans4(wrongAnswerList.get(2)).build());
		}
		workbookRepo.saveAll(workbooks);
		return true;
	}

}
