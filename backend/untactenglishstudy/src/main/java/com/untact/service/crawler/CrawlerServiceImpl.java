package com.untact.service.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.untact.antonym.domain.Antonym;
import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.thesaurus.domain.Thesaurus;

import lombok.extern.java.Log;

@Service
@Log
public class CrawlerServiceImpl implements CrawlerService {

	@Override
	public List<EnglishDictionary> getMeaning(Set<String> notExistWord){
		List<EnglishDictionary> ret = new ArrayList<>();
		for(String word:notExistWord) {
			String wordclass = "";
			String meaning = "";
			try {
				String URL = "http://endic.naver.com/search.nhn?query=" + word;
				Document doc = Jsoup.connect(URL).header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36").get();
				wordclass = doc.selectFirst("dl.list_e2").selectFirst("dd").selectFirst("span.fnt_k09").text();
				meaning = doc.selectFirst("dl.list_e2").selectFirst("dd").selectFirst("span.fnt_k05").text();
				if(meaning.length() == 0) {
					wordclass = "NOTFOUND";
					meaning = "NOTFOUND";
				}
			}catch(Exception e) {
				wordclass = "NOTFOUND";
				meaning = "NOTFOUND";
			}
			ret.add(EnglishDictionary.builder()
					.englishSpelling(EnglishSpelling.builder().spelling(word).build())
					.meaning(meaning)
					.partOfSpeech(wordclass)
					.build());
		}
		return ret;
	}

	@Override
	public List<Thesaurus> getThesaurus(Set<String> notExistWord) {
		List<Thesaurus> ret = new ArrayList<>();
		for(String word:notExistWord) {
			try {
				String URL = "https://www.thesaurus.com/browse/" + word;
				Document doc = Jsoup.connect(URL).header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36").get();
				Elements elements = doc.select("a.css-1s3v085.eh475bn1");
				for(Element elem:elements) {
					ret.add(Thesaurus.builder()
							.englishSpelling(EnglishSpelling.builder().spelling(word).build())
							.wordto(elem.text())
							.build());
				}
			}catch(Exception e) {
				
			}
		}
		return ret;
	}

	@Override
	public List<Antonym> getAntonym(Set<String> notExistWord) {
		List<Antonym> ret = new ArrayList<>();
		for(String word:notExistWord) {
			try {
				String URL = "https://www.thesaurus.com/browse/" + word;
				Document doc = Jsoup.connect(URL).header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36").get();
				Elements elements = doc.select("a.css-itvgb.eh475bn1");
				for(Element elem:elements) {
					ret.add(Antonym.builder()
							.englishSpelling(EnglishSpelling.builder().spelling(word).build())
							.wordto(elem.text())
							.build());
				}
			}catch(Exception e) {
				
			}
		}
		return ret;
	}
}
