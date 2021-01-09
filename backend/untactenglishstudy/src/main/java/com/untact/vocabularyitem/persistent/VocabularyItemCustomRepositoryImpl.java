package com.untact.vocabularyitem.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.JPQLQuery;
import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.vocabularyitem.domain.QVocabularyItem;
import com.untact.vocabularyitem.domain.VocabularyItem;

public class VocabularyItemCustomRepositoryImpl extends QuerydslRepositorySupport
		implements VocabularyItemCustomRepository {
	public VocabularyItemCustomRepositoryImpl() {
		super(VocabularyItem.class);
	}

	@Override
	public List<EnglishSpelling> findEnglishSpellingByVocaburaryNumber(Long vno) {
		QVocabularyItem item = QVocabularyItem.vocabularyItem;
		JPQLQuery<EnglishSpelling> query = from(item).select(item.englishSpelling);
		query.where(item.vocabulary.vno.eq(vno));
		return query.fetch();
	}
}
