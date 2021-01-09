package com.untact.reply.persistent;

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
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.persistent.util.DeleteAllUtil;
import com.untact.reply.domain.Reply;
import com.untact.reply.persistent.ReplyRepository;
import com.untact.vo.PageVO;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class ReplyRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	@Autowired
	private GroupEntityRepository groupRepo;
	
	@Autowired
	private BoardRepository boardRepo;
	
	@Autowired
	private ReplyRepository replyRepo;
	
	private GroupEntity group1;
	
	private Board board1;
	
	private static final int MAX_ENTITY_COUNT = 105;
	private static final int EXPECTED_PAGE_COUNT = 11;
	
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
		
		group1 = new GroupEntity().builder().title("title").build();
		groupRepo.save(group1);
		
		board1 = new Board().builder().title("title").content("content").build();
		boardRepo.save(board1);
	}
	
	
	@Test
	public void initTest() {
		
	}
	@Test
	public void getPageWithBoardNumberTest() {
		List<Reply> list = generateReplyList(group1,board1);
		specificPageTest(list,board1,1,10);
		specificPageTest(list,board1,11,5);
	}
	
	private List<Reply> generateReplyList(GroupEntity group,Board board){
		List<Reply> list = new ArrayList<>();
		for(int i=0;i<MAX_ENTITY_COUNT;i++) {
			Reply entity = generateReply("message"+i,group,board);
			list.add(entity);
		}
		replyRepo.saveAll(list);
		return list;
	}
	private Reply generateReply(String message,GroupEntity group,Board board) {
		return new Reply().builder().message(message).group(group).board(board).build();
	}
	
	private void specificPageTest(List<Reply> list,Board board,int pageNum,int expectedPageSize) {
		PageVO pageVO = new PageVO(pageNum);
		Page<Reply> page = replyRepo.getPageWithBoardNumber(pageVO.makePageable(0, "rno"),board.getBno());
		List<Reply> result = page.getContent();
		assertEquals(page.getTotalElements(),MAX_ENTITY_COUNT);
		assertEquals(page.getTotalPages(),EXPECTED_PAGE_COUNT);
		assertEquals(result.size(),expectedPageSize);
		assertEquals(page.getNumber(),pageNum-1);
		int resultIdx = 0;
		for(int i = list.size()-1-pageVO.getSize()*(pageNum-1);i>Math.max(-1,list.size()-1-pageVO.getSize()*pageNum);i--) {
			//list의 첫번째 원소는 가장 처음에, list의 마지막 원소는 가장 나중에 들어옴
			//내림차순으로 정렬되었는지 확인하기 위함
			assertTrue(result.get(resultIdx).getMessage().equals(list.get(i).getMessage()));
			resultIdx++;
		}
	}
}
