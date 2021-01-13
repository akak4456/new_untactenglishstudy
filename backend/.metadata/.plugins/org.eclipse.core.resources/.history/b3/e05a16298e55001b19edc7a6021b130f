package com.untact.antonym.persistent;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.antonym.domain.Antonym;
import com.untact.demo.UntactenglishstudyApplication;
import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.englishspelling.domain.EnglishSpellingDifficulty;
import com.untact.englishspelling.persistent.EnglishSpellingRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
public class AntonymRepositoryTest {
	/*
	 * spring.jpa.hibernate.ddl-auto=create-drop
	 * 로 설정해서 테스트 할 것
	 */
	
	@Autowired
	private AntonymRepository antonymRepo;
	
	private EnglishSpelling englishSpelling;
	
	@Before
	public void setUp() {
		assertEquals(antonymRepo.count(),0L);
		
		englishSpelling = EnglishSpelling.builder().spelling("spelling").lv(EnglishSpellingDifficulty.easy).build();
	}
	@Test
	public void successSaveTest() {
		Antonym antonym = Antonym.builder().englishSpelling(englishSpelling).wordto("word").build();
		antonymRepo.saveAndFlush(antonym);
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void wordFromNullSaveTest() {
		Antonym antonym = Antonym.builder().wordto("word").build();
		antonymRepo.saveAndFlush(antonym);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void wordToNullSaveTest() {
		Antonym antonym = Antonym.builder().englishSpelling(englishSpelling).build();
		antonymRepo.saveAndFlush(antonym);
	}
}
