package com.untact.englishdictionary.persistent;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.demo.UntactenglishstudyApplication;
import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.englishdictionary.persistent.EnglishDictionaryRepository;
import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.englishspelling.persistent.EnglishSpellingRepository;
import com.untact.persistent.util.DeleteAllUtil;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class EnglishDictionaryRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	
	@Autowired
	private EnglishDictionaryRepository englishDictionaryRepo;
	
	@Autowired
	private EnglishSpellingRepository englishSpellingRepo;
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
	}
	
	@Test
	public void initTest() {
		
	}
	
	@Test
	public void insertTest() {
		EnglishSpelling spell = EnglishSpelling.builder().spelling("a").build();
		englishSpellingRepo.save(spell);
		
		englishDictionaryRepo.save(EnglishDictionary.builder().englishSpelling(spell).meaning("가나다").build());
		englishDictionaryRepo.save(EnglishDictionary.builder().englishSpelling(spell).meaning("마바사").build());
		assertEquals(englishDictionaryRepo.count(),2L);
	}
	
	@Test
	public void insertDuplicateTest() {
		EnglishSpelling spell = EnglishSpelling.builder().spelling("a").build();
		englishSpellingRepo.save(spell);
		englishDictionaryRepo.save(EnglishDictionary.builder().englishSpelling(spell).meaning("가나다").build());
		englishDictionaryRepo.save(EnglishDictionary.builder().englishSpelling(spell).meaning("가나다").build());
		englishDictionaryRepo.save(EnglishDictionary.builder().englishSpelling(spell).meaning("가나다").build());
		assertEquals(englishDictionaryRepo.count(),1L);
	}
}
