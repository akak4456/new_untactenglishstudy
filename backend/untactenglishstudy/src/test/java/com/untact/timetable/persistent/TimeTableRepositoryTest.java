package com.untact.timetable.persistent;

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
import com.untact.demo.UntactenglishstudyApplication;
import com.untact.file.persistent.FileEntityRepository;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.persistent.util.DeleteAllUtil;
import com.untact.persistent.util.Range;
import com.untact.timetable.domain.TimeTable;
import com.untact.timetable.persistent.TimeTableRepository;
import com.untact.vo.PageVO;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class TimeTableRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	@Autowired
	private GroupEntityRepository groupRepo;
	
	@Autowired
	private TimeTableRepository timeTableRepo;
	
	@Autowired
	private MemberEntityRepository memberRepo;
	
	private GroupEntity group1;
	private GroupEntity group2;
	
	private MemberEntity member1;
	private MemberEntity member2;
	private MemberEntity member3;
	
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
		group1 = new GroupEntity().builder().title("title1").build();
		groupRepo.save(group1);
		group2 = new GroupEntity().builder().title("title2").build();
		groupRepo.save(group2);
		member1 = MemberEntity.builder().name("name1").build();
		memberRepo.save(member1);
		member2 = MemberEntity.builder().name("name2").build();
		memberRepo.save(member2);
		member3 = MemberEntity.builder().name("name3").build();
		memberRepo.save(member3);
	}
	@Test
	public void initTest() {
	}
	
	@Test
	public void getPageWithGroupNumberTest() {
		List<TimeTable> timeTables = new ArrayList<>();
		addTimeTables(timeTables,group1,member1,5);
		addTimeTables(timeTables,group1,member2,15);
		addTimeTables(timeTables,group1,member3,19);
		addTimeTables(timeTables,group2,member1,21);
		addTimeTables(timeTables,group2,member2,1);
		addTimeTables(timeTables,group2,member3,11);
		timeTableRepo.saveAll(timeTables);
		specificPageTest(
				timeTables,
				group1,
				member1,
				1,
				5,
				1,
				5,
				Range.of(1,5)
		);
		specificPageTest(
				timeTables,
				group1,
				member2,
				1,
				15,
				2,
				10,
				Range.of(10,20)
		);
		specificPageTest(
				timeTables,
				group1,
				member2,
				2,
				15,
				2,
				5,
				Range.of(5,10)
		);
		specificPageTest(
				timeTables,
				group1,
				member3,
				1,
				19,
				2,
				10,
				Range.of(29,39)
		);
		specificPageTest(
				timeTables,
				group1,
				member3,
				2,
				19,
				2,
				9,
				Range.of(20,29)
		);
		specificPageTest(
				timeTables,
				group2,
				member1,
				1,
				21,
				3,
				10,
				Range.of(50,60)
		);
		specificPageTest(
				timeTables,
				group2,
				member1,
				2,
				21,
				3,
				10,
				Range.of(40,50)
		);
		specificPageTest(
				timeTables,
				group2,
				member1,
				3,
				21,
				3,
				1,
				Range.of(39,40)
		);
		specificPageTest(
				timeTables,
				group2,
				member2,
				1,
				1,
				1,
				1,
				Range.of(60,61)
		);
		specificPageTest(
				timeTables,
				group2,
				member3,
				1,
				11,
				2,
				10,
				Range.of(62,72)
		);
		specificPageTest(
				timeTables,
				group2,
				member3,
				2,
				11,
				2,
				1,
				Range.of(61,62)
		);
		
		
	}
	private void addTimeTables(List<TimeTable> list, GroupEntity group,MemberEntity member, int count) {
		for(int i=0;i<count;i++) {
			list.add(TimeTable.builder().title("title"+i).group(group).member(member).build());
		}
	}
	
	
	private void specificPageTest(
			List<TimeTable> list,
			GroupEntity group,
			MemberEntity member,
			int pageNum,
			int expectedTotalElementsCount,
			int expectedTotalPages,
			int expectedPageSize,
			Range range) {
		PageVO pageVO = new PageVO(pageNum);
		Page<TimeTable> page = timeTableRepo.getPageWithGroupNumberAndMemberNumber(pageVO.makePageable(0, "tno"),group.getGno(), member.getMno());
		List<TimeTable> result = page.getContent();
		assertEquals(page.getTotalElements(),expectedTotalElementsCount);
		assertEquals(page.getTotalPages(),expectedTotalPages);
		assertEquals(result.size(),expectedPageSize);
		assertEquals(page.getNumber(),pageNum-1);
		int resultIdx = 0;
		for(int i=range.getEnd()-1;i>=range.getStart();i--) {
			log.info(result.get(resultIdx).getTitle());
			log.info(list.get(i).getTitle());
			assertTrue(result.get(resultIdx).getTitle().equals(list.get(i).getTitle()));
			resultIdx++;
		}
	}
}
