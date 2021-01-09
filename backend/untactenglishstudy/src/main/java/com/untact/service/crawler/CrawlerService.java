package com.untact.service.crawler;

import java.util.List;
import java.util.Set;

import com.untact.antonym.domain.Antonym;
import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.thesaurus.domain.Thesaurus;

public interface CrawlerService {
	public List<EnglishDictionary> getMeaning(Set<String> notExistWord);
	public List<Thesaurus> getThesaurus(Set<String> notExistWord);
	public List<Antonym> getAntonym(Set<String> notExistWord);
}
