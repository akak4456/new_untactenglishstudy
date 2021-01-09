package com.untact.board.persistent;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.board.domain.Board;
import com.untact.board.persistent.BoardRepository;
import com.untact.demo.UntactenglishstudyApplication;
import com.untact.file.persistent.FileEntityRepository;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.persistent.util.DeleteAllUtil;
import com.untact.vo.PageVO;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class BoardRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	@Autowired
	private GroupEntityRepository groupRepo;
	
	@Autowired
	private BoardRepository boardRepo;
	
	@Autowired
	private MemberEntityRepository memberRepo;
	
	@Autowired
	private FileEntityRepository fileRepo;
	
	private static final int MAX_ENTITY_COUNT = 105;
	private static final int EXPECTED_PAGE_COUNT = 11;
	
	private GroupEntity group1;
	
	private MemberEntity member1;
	
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
		group1 = new GroupEntity().builder().title("title").build();
		groupRepo.save(group1);
		member1 = MemberEntity.builder().name("name").build();
		memberRepo.save(member1);
	}
	@Test
	public void initTest() {
	}
	
	@Test
	public void getPageWithGroupNumberTest() {
		List<Board> list = generateBoardList(group1,member1);
		specificPageTest(list,group1,1,10);
		specificPageTest(list,group1,11,5);
	}
	
	private List<Board> generateBoardList(GroupEntity group,MemberEntity member){
		List<Board> list = new ArrayList<>();
		for(int i=0;i<MAX_ENTITY_COUNT;i++) {
			Board entity = generateBoard("title"+i,"content"+i,group,member);
			list.add(entity);
		}
		boardRepo.saveAll(list);
		return list;
	}
	private Board generateBoard(String title,String content,GroupEntity group,MemberEntity member) {
		return new Board().builder().title(title).content(content).group(group).member(member).build();
	}
	
	private void specificPageTest(List<Board> list,GroupEntity group,int pageNum,int expectedPageSize) {
		PageVO pageVO = new PageVO(pageNum);
		Page<Board> page = boardRepo.getPageWithGroupNumber(pageVO.makePageable(0, "bno"),group.getGno());
		List<Board> result = page.getContent();
		assertEquals(page.getTotalElements(),MAX_ENTITY_COUNT);
		assertEquals(page.getTotalPages(),EXPECTED_PAGE_COUNT);
		assertEquals(result.size(),expectedPageSize);
		assertEquals(page.getNumber(),pageNum-1);
		int resultIdx = 0;
		for(int i = list.size()-1-pageVO.getSize()*(pageNum-1);i>Math.max(-1,list.size()-1-pageVO.getSize()*pageNum);i--) {
			//list의 첫번째 원소는 가장 처음에, list의 마지막 원소는 가장 나중에 들어옴
			//내림차순으로 정렬되었는지 확인하기 위함
			assertTrue(result.get(resultIdx).getTitle().equals(list.get(i).getTitle()));
			log.info(result.get(resultIdx).getTitle());
			log.info(result.get(resultIdx).getMember().getName());
			resultIdx++;
		}
	}
}
