package com.untact.englishdictionary.persistent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.untact.englishdictionary.domain.EnglishDictionary;

public interface EnglishDictionaryCustomRepository {
	public Page<EnglishDictionary> getPageWithVocabularyNumber(Pageable pageable,Long vno);
}
