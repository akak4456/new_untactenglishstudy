package com.untact.vocabularyitem.persistent;

import java.util.List;

import com.untact.englishspelling.domain.EnglishSpelling;

public interface VocabularyItemCustomRepository {
	public List<EnglishSpelling> findEnglishSpellingByVocaburaryNumber(Long vno);
}
