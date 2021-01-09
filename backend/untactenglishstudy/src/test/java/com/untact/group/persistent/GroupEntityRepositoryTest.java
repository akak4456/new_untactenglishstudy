package com.untact.group.persistent;

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

import com.untact.demo.UntactenglishstudyApplication;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.member.domain.Role;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.persistent.util.DeleteAllUtil;
import com.untact.persistent.util.Range;
import com.untact.vo.PageVO;

import lombok.extern.java.Log;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class GroupEntityRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	
	@Autowired
	private MemberEntityRepository memberRepo;
	
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	
	@Autowired
	private GroupEntityRepository groupRepo;
	
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
		groupRepo.deleteAllInBatch();
	}
	
	@Test
	public void initTest() {
		
	}
	
	private static final int MAX_ENTITY_COUNT = 105;
	private static final int MAX_MEMBER_COUNT = 10;
	private static final int EXPECTED_PAGE_COUNT = 11;
	
	@Test
	public void getPageTest() {
		List<GroupEntity> list = generateGroupList();
		specificPageTest(list,1,10);
		specificPageTest(list,11,5);
	}
	
	private List<GroupEntity> generateGroupList(){
		List<GroupEntity> list = new ArrayList<>();
		for(int i=0;i<MAX_ENTITY_COUNT;i++) {
			GroupEntity entity = generateGroupEntity("title"+i);
			list.add(entity);
		}
		groupRepo.saveAll(list);
		return list;
	}
	private GroupEntity generateGroupEntity(String title) {
		return new GroupEntity().builder().title(title).build();
	}
	private void specificPageTest(List<GroupEntity> list,int pageNum,int expectedPageSize) {
		PageVO pageVO = new PageVO(pageNum);
		Page<GroupEntity> page = groupRepo.getPage(pageVO.makePageable(0, "gno"));
		List<GroupEntity> result = page.getContent();
		assertEquals(page.getTotalElements(),MAX_ENTITY_COUNT);
		assertEquals(page.getTotalPages(),EXPECTED_PAGE_COUNT);
		assertEquals(result.size(),expectedPageSize);
		assertEquals(page.getNumber(),pageNum-1);
		int resultIdx = 0;
		for(int i = list.size()-1-pageVO.getSize()*(pageNum-1);i>Math.max(-1,list.size()-1-pageVO.getSize()*pageNum);i--) {
			//list의 첫번째 원소는 가장 처음에, list의 마지막 원소는 가장 나중에 들어옴
			//내림차순으로 정렬되었는지 확인하기 위함
			assertTrue(result.get(resultIdx).getTitle().equals(list.get(i).getTitle()));
			resultIdx++;
		}
	}
	@Test
	public void getPageWithUserNumberTest() {
		List<GroupEntity> groupList = generateGroupList();
		List<MemberEntity> memberList = generateMemberList();
		List<Range> rangeList = new ArrayList<>();//member i는 어느 그룹부터 어느 그룹까지 가입했는가?
		rangeList.add(Range.of(0, 10));//start <= i < end임 이것은 0번째 member가 0번에서 4번그룹까지 가입했음을 의미함
		rangeList.add(Range.of(10, 20));
		rangeList.add(Range.of(20, 40));
		rangeList.add(Range.of(40,70));
		rangeList.add(Range.of(70,85));
		rangeList.add(Range.of(85,90));
		rangeList.add(Range.of(90,101));
		List<GroupInclude> groupIncludeList = generateGroupIncludeList(groupList,memberList,rangeList);
		specificPageWithUserNumberTest(groupList,memberList.get(0),1,10,1,10,rangeList.get(0));
		specificPageWithUserNumberTest(groupList,memberList.get(1),1,10,1,10,rangeList.get(1));
		specificPageWithUserNumberTest(groupList,memberList.get(2),1,20,2,10,Range.of(30, 40));
		specificPageWithUserNumberTest(groupList,memberList.get(2),2,20,2,10,Range.of(20, 30));
		specificPageWithUserNumberTest(groupList,memberList.get(3),1,30,3,10,Range.of(60, 70));
		specificPageWithUserNumberTest(groupList,memberList.get(3),2,30,3,10,Range.of(50, 60));
		specificPageWithUserNumberTest(groupList,memberList.get(3),3,30,3,10,Range.of(40, 50));
		specificPageWithUserNumberTest(groupList,memberList.get(4),1,15,2,10,Range.of(75, 85));
		specificPageWithUserNumberTest(groupList,memberList.get(4),2,15,2,5,Range.of(70, 75));
		specificPageWithUserNumberTest(groupList,memberList.get(5),1,5,1,5,Range.of(85, 90));
		specificPageWithUserNumberTest(groupList,memberList.get(6),1,11,2,10,Range.of(91, 101));
		specificPageWithUserNumberTest(groupList,memberList.get(6),2,11,2,1,Range.of(90, 91));
	}
	private List<MemberEntity> generateMemberList(){
		List<MemberEntity> list = new ArrayList<>();
		for(int i=0;i<MAX_MEMBER_COUNT;i++) {
			MemberEntity entity = generateMemberEntity("email"+i+"@naver.com","1234");
			list.add(entity);
		}
		memberRepo.saveAll(list);
		return list;
	}
	private MemberEntity generateMemberEntity(String email,String password) {
		return new MemberEntity().builder().email(email).password(password).role(Role.MEMBER).build();
	}
	private List<GroupInclude> generateGroupIncludeList(List<GroupEntity> groupList,List<MemberEntity> memberList,List<Range> rangeList){
		List<GroupInclude> list = new ArrayList<>();
		//첫번째 member는 1~5번 그룹에 가입한다
		int cnt = 0;
		for(Range range:rangeList) {
			for(int i=range.getStart();i<range.getEnd();i++) {
				list.add(generateGroupInclude(groupList.get(i),memberList.get(cnt)));
			}
			cnt++;
		}
		groupIncludeRepo.saveAll(list);
		return list;
	}
	private GroupInclude generateGroupInclude(GroupEntity group,MemberEntity member) {
		return new GroupInclude().builder().group(group).member(member).build();
	}
	private void specificPageWithUserNumberTest(
			List<GroupEntity> groupList,
			MemberEntity member,
			int pageNum,
			int expectedTotalElementsCount,
			int expectedTotalPages,
			int expectedPageSize,
			Range range) {
		PageVO pageVO = new PageVO(pageNum);
		Page<GroupEntity> page = groupRepo.getPageWithUserNumber(pageVO.makePageable(0, "gno"), member.getMno());
		List<GroupEntity> result = page.getContent();
		assertEquals(page.getTotalElements(),expectedTotalElementsCount);
		assertEquals(page.getTotalPages(),expectedTotalPages);
		assertEquals(result.size(),expectedPageSize);
		assertEquals(page.getNumber(),pageNum-1);
		int resultIdx = 0;
		for(int i=range.getEnd()-1;i>=range.getStart();i--) {
			assertTrue(result.get(resultIdx).getTitle().equals(groupList.get(i).getTitle()));
			resultIdx++;
		}
	}
}
