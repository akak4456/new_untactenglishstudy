package com.untact.groupinclude.persistent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.demo.UntactenglishstudyApplication;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.domain.WhichStatus;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.persistent.util.DeleteAllUtil;
import com.untact.vo.GroupAndMemberVO;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Commit
@Log
public class GroupIncludeRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	@Autowired
	private MemberEntityRepository memberRepo;
	@Autowired
	private GroupEntityRepository groupRepo;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	private MemberEntity member1;
	private MemberEntity member2;
	private MemberEntity member3;
	
	private GroupEntity group1;
	private GroupEntity group2;
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
		member1 = MemberEntity.builder().email("email1").password("password1").build();
		memberRepo.save(member1);
		member2 = MemberEntity.builder().email("email2").password("password2").build();
		memberRepo.save(member2);
		member3 = MemberEntity.builder().email("email3").password("password3").build();
		memberRepo.save(member3);

		group1 = GroupEntity.builder().title("title1").build();
		groupRepo.save(group1);
		group2 = GroupEntity.builder().title("title2").build();
		groupRepo.save(group2);
	}
	@Test
	public void initTest() {
	
	}
	
	@Test
	public void changeStatusTest() {
		GroupInclude include = new GroupInclude().builder().group(group1).member(member1).whichStatus(WhichStatus.WAITING).build();
		groupIncludeRepo.save(include);
		Long gino = include.getGino();
		assertEquals(groupIncludeRepo.findById(gino).get().getWhichStatus(),WhichStatus.WAITING);
		groupIncludeRepo.updateStatusByGroupIncludeNumber(WhichStatus.FOLLOWER, gino);
		assertEquals(groupIncludeRepo.findById(gino).get().getWhichStatus(),WhichStatus.FOLLOWER);
		groupIncludeRepo.updateStatusByGroupIncludeNumber(WhichStatus.REJECT, gino);
		assertEquals(groupIncludeRepo.findById(gino).get().getWhichStatus(),WhichStatus.REJECT);
	}
	
	@Test
	public void findByGroupAndMemberAndWhichStatusTest() {
		
		groupIncludeRepo.save(GroupInclude.builder().group(group1).member(member1).whichStatus(WhichStatus.LEADER).build());
		groupIncludeRepo.save(GroupInclude.builder().group(group1).member(member2).whichStatus(WhichStatus.FOLLOWER).build());
		groupIncludeRepo.save(GroupInclude.builder().group(group1).member(member3).whichStatus(WhichStatus.FOLLOWER).build());
	
		groupIncludeRepo.save(GroupInclude.builder().group(group2).member(member1).whichStatus(WhichStatus.FOLLOWER).build());
		groupIncludeRepo.save(GroupInclude.builder().group(group2).member(member2).whichStatus(WhichStatus.LEADER).build());
		groupIncludeRepo.save(GroupInclude.builder().group(group2).member(member3).whichStatus(WhichStatus.FOLLOWER).build());
		
		assertTrue(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group1.getGno(),member1.getMno(), WhichStatus.LEADER).isPresent());
		assertFalse(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group1.getGno(),member2.getMno(), WhichStatus.LEADER).isPresent());
		assertFalse(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group1.getGno(),member3.getMno(), WhichStatus.LEADER).isPresent());
		
		assertFalse(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group2.getGno(),member1.getMno(), WhichStatus.LEADER).isPresent());
		assertTrue(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group2.getGno(),member2.getMno(), WhichStatus.LEADER).isPresent());
		assertFalse(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group2.getGno(),member3.getMno(), WhichStatus.LEADER).isPresent());
		
		assertFalse(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group1.getGno(),member1.getMno(), WhichStatus.FOLLOWER).isPresent());
		assertTrue(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group1.getGno(),member2.getMno(), WhichStatus.FOLLOWER).isPresent());
		assertTrue(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group1.getGno(),member3.getMno(), WhichStatus.FOLLOWER).isPresent());
		
		assertTrue(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group2.getGno(),member1.getMno(), WhichStatus.FOLLOWER).isPresent());
		assertFalse(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group2.getGno(),member2.getMno(), WhichStatus.FOLLOWER).isPresent());
		assertTrue(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(group2.getGno(),member3.getMno(), WhichStatus.FOLLOWER).isPresent());
	}
	
	@Test
	public void findMemberByGroupNumbersTest() {
		groupIncludeRepo.save(GroupInclude.builder().group(group1).member(member1).whichStatus(WhichStatus.LEADER).build());
		groupIncludeRepo.save(GroupInclude.builder().group(group1).member(member2).whichStatus(WhichStatus.FOLLOWER).build());
		groupIncludeRepo.save(GroupInclude.builder().group(group1).member(member3).whichStatus(WhichStatus.FOLLOWER).build());
	
		groupIncludeRepo.save(GroupInclude.builder().group(group2).member(member1).whichStatus(WhichStatus.FOLLOWER).build());
		groupIncludeRepo.save(GroupInclude.builder().group(group2).member(member2).whichStatus(WhichStatus.LEADER).build());
		List<Long> id1 = List.of(group1.getGno());
		List<GroupInclude> memberList1 = groupIncludeRepo.findByGroupNumbers(id1);
		assertEquals(memberList1.size(),3);
		List<Long> id2 = List.of(group2.getGno());
		List<GroupInclude> memberList2 = groupIncludeRepo.findByGroupNumbers(id2);
		assertEquals(memberList2.size(),2);
	}
	
}
