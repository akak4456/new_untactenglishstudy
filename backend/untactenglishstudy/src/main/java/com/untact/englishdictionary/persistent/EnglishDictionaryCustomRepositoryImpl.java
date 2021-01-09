package com.untact.englishdictionary.persistent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.englishdictionary.domain.QEnglishDictionary;
import com.untact.englishspelling.domain.QEnglishSpelling;
import com.untact.vocabularyitem.domain.QVocabularyItem;

public class EnglishDictionaryCustomRepositoryImpl extends QuerydslRepositorySupport
		implements EnglishDictionaryCustomRepository {	
	public EnglishDictionaryCustomRepositoryImpl() {
		super(EnglishDictionary.class);
	}

	@Override
	public Page<EnglishDictionary> getPageWithVocabularyNumber(Pageable pageable, Long vno) {
		QEnglishDictionary englishDictionary = QEnglishDictionary.englishDictionary;
		QEnglishSpelling spelling = QEnglishSpelling.englishSpelling;
		List<Tuple> list = null;
		Long totalCount = 0L;
		if(vno == 0L) {
			//기본단어장에 있는 단어들을 원하면
			JPQLQuery<Tuple> query = from(englishDictionary).select(englishDictionary.edno,englishDictionary.englishSpelling,englishDictionary.meaning,englishDictionary.partOfSpeech);
			query.innerJoin(englishDictionary.englishSpelling,spelling);
			query.where(englishDictionary.meaning.notEqualsIgnoreCase("NOTFOUND"));
			totalCount = query.fetchCount();
			list = getQuerydsl().applyPagination(pageable, query).fetch();
		}else {
			QVocabularyItem vocabularyItem = QVocabularyItem.vocabularyItem;
			JPQLQuery<Tuple> query = from(englishDictionary).select(englishDictionary.edno,englishDictionary.englishSpelling,englishDictionary.meaning,englishDictionary.partOfSpeech);
			query.innerJoin(englishDictionary.englishSpelling,spelling);
			query.where(englishDictionary.englishSpelling.spelling.in(
					JPAExpressions.select(vocabularyItem.englishSpelling.spelling)
					.from(vocabularyItem)
					.where(vocabularyItem.vocabulary.vno.eq(vno))
					).and(englishDictionary.meaning.notEqualsIgnoreCase("NOTFOUND")));
			totalCount = query.fetchCount();
			list = getQuerydsl().applyPagination(pageable, query).fetch();
		}
		List<EnglishDictionary> englishDictionaryList = new ArrayList<>();
		for(Tuple t:list) {
			englishDictionaryList.add(EnglishDictionary.builder()
					.edno(t.get(englishDictionary.edno))
					.englishSpelling(t.get(englishDictionary.englishSpelling))
					.meaning(t.get(englishDictionary.meaning))
					.partOfSpeech(t.get(englishDictionary.partOfSpeech))
					.build());
		}
		return new PageImpl<EnglishDictionary>(englishDictionaryList,pageable,totalCount);
	}
}
