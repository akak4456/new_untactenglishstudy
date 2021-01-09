package com.untact.englishspelling.persistent;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.demo.UntactenglishstudyApplication;
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
public class EnglishSpellingRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	@Autowired
	private EnglishDictionaryRepository englishDictionaryRepo;
	
	@Autowired
	private EnglishSpellingRepository englishSpellingRepo;
	
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
		List<EnglishSpelling> spellingList = new ArrayList<>();
		EnglishSpelling entity1 = new EnglishSpelling().builder().spelling("a").build();
		spellingList.add(entity1);
		EnglishSpelling entity2 = new EnglishSpelling().builder().spelling("b").build();
		spellingList.add(entity2);
		EnglishSpelling entity3 = new EnglishSpelling().builder().spelling("c").build();
		spellingList.add(entity3);
		EnglishSpelling entity4 = new EnglishSpelling().builder().spelling("d").build();
		spellingList.add(entity4);
		EnglishSpelling entity5 = new EnglishSpelling().builder().spelling("e").build();
		spellingList.add(entity5);
		englishSpellingRepo.saveAll(spellingList);
	}
	
	@Test
	public void initTest() {
	}
	@Test
	public void EnglishSpellingListNotExistInDBTest() {
		List<String> targetSpellings = new ArrayList<>();
		targetSpellings.add("a");
		targetSpellings.add("b");
		targetSpellings.add("f");
		List<EnglishSpelling> result = englishSpellingRepo.findByTargetSpellingList(targetSpellings);
		assertEquals(result.get(0).getSpelling(),"a");
		assertEquals(result.get(1).getSpelling(),"b");
		assertEquals(result.size(),2);
	}
}
