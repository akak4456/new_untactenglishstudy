package com.untact.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.untact.demo.UntactenglishstudyApplication;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.member.domain.Role;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.vo.MemberVO;
import com.untact.vo.NumberOfSuccessesOrFailures;
import com.untact.vocabulary.persistent.VocabularyRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@AutoConfigureMockMvc
public class VocabularyControllerTest {
	@Autowired
	private GroupEntityRepository  groupRepo;
	@Autowired 
	private MemberEntityRepository memberRepo;
	@Autowired
	private VocabularyRepository vocabularyRepo;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MockMvc mockMvc;
	
	private MemberEntity member;
	private GroupEntity group;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setUp() {
		vocabularyRepo.deleteAllInBatch();
		groupIncludeRepo.deleteAllInBatch();
		memberRepo.deleteAllInBatch();
		groupRepo.deleteAllInBatch();
		member = new MemberEntity().builder().email("akak4456@naver.com").password(passwordEncoder.encode("1234")).role(Role.MEMBER).build();
		group = new GroupEntity().builder().title("title").build();
		memberRepo.save(member);
		groupRepo.save(group);
		groupIncludeRepo.save(new GroupInclude().builder().member(member).group(group).build());
	}
	
	@Test
	public void addVocabularyTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    
	    //로그인 부분
	    MemberVO vo = new MemberVO();
	    vo.setEmail("akak4456@naver.com");
	    vo.setPassword("1234");
	    String requestJson=ow.writeValueAsString(vo);
	    System.out.println(requestJson);
		MvcResult result = mockMvc.perform(post("/member/login").contentType(APPLICATION_JSON_UTF8)
	            .content(requestJson))
	            .andExpect(status().isOk()).andReturn();
		
		String token = result.getResponse().getContentAsString();
		//로그인 부분
		
		/*
		 english_spelling entity에
		"angry"
		"apple"
		"banana"
		"beautiful"
		"sorry"
		 english_dictionary entity에
		 "미안한"	"sorry"
"바나나"	"banana"
"분개한"	"angry"
"사과"	"apple"
"수려한"	"beautiful"
"아름다운"	"beautiful"
"화난"	"angry"

가 있다고 가정한다.
		 */
		String url = "/vocabulary/"+group.getGno()+"/"+member.getMno();
		List<String> wordList = new ArrayList<>();
		wordList.add("apple");
		wordList.add("banana");
	    requestJson=ow.writeValueAsString(wordList);
	    mockMvc.perform(
	    		post(url)
	    		.contentType(APPLICATION_JSON_UTF8)
	    		.header("X-AUTH-TOKEN", token)
	            .content(requestJson))
	            .andExpect(status().isOk())
	            .andExpect(content().json("{\"numberOfSuccesses\":2,\"numberOfFailures\":0}"));
	    assertEquals(vocabularyRepo.count(),2L);
	    
	    wordList.clear();
	    wordList.add("angry");
	    wordList.add("beautiful");
	    wordList.add("sorry");
	    requestJson=ow.writeValueAsString(wordList);
	    vocabularyRepo.deleteAllInBatch();
	    mockMvc.perform(
	    		post(url)
	    		.contentType(APPLICATION_JSON_UTF8)
	    		.header("X-AUTH-TOKEN", token)
	            .content(requestJson))
	            .andExpect(status().isOk())
	            .andExpect(content().json("{\"numberOfSuccesses\":3,\"numberOfFailures\":0}"));
	    assertEquals(vocabularyRepo.count(),3L);
	    
	    wordList.clear();
	    wordList.add("angry");
	    wordList.add("beautiful");
	    wordList.add("abcdefg");
	    requestJson=ow.writeValueAsString(wordList);
	    vocabularyRepo.deleteAllInBatch();
	    mockMvc.perform(
	    		post(url)
	    		.contentType(APPLICATION_JSON_UTF8)
	    		.header("X-AUTH-TOKEN", token)
	            .content(requestJson))
	            .andExpect(status().isOk())
	            .andExpect(content().json("{\"numberOfSuccesses\":2,\"numberOfFailures\":1}"));
	    assertEquals(vocabularyRepo.count(),2L);
	}
}
