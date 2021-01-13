package com.untact.attendance.persistent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.attendance.domain.Attendance;
import com.untact.attendance.domain.AttendanceStatus;
import com.untact.demo.UntactenglishstudyApplication;
import com.untact.group.domain.GroupEntity;
import com.untact.member.domain.MemberEntity;
import com.untact.persistent.util.PageTester;
import com.untact.vo.PageDirection;
import com.untact.vo.PageVO;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Log
public class AttendanceRepositoryTest {
	/*
	 * spring.jpa.hibernate.ddl-auto=create-drop 로 설정해서 테스트 할 것
	 */

	@Autowired
	private AttendanceRepository attendanceRepo;

	private List<GroupEntity> group;

	private List<MemberEntity> member;

	@Before
	public void setUp() {
		group = new ArrayList<>();
		member = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			group.add(GroupEntity.builder().title("title" + i).build());
			member.add(MemberEntity.builder().email("email" + i + "@email.com").build());
		}
	}

	@Test
	public void successSaveTest() {
		Attendance attendance = Attendance.builder().status(AttendanceStatus.ABSENT).group(group.get(0))
				.member(member.get(0)).build();
		attendanceRepo.saveAndFlush(attendance);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void statusNullSaveTest() {
		Attendance attendance = Attendance.builder().group(group.get(0)).member(member.get(0)).build();
		attendanceRepo.saveAndFlush(attendance);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void groupNullSaveTest() {
		Attendance attendance = Attendance.builder().status(AttendanceStatus.ABSENT).member(member.get(0)).build();
		attendanceRepo.saveAndFlush(attendance);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void memberNullSaveTest() {
		Attendance attendance = Attendance.builder().status(AttendanceStatus.ABSENT).group(group.get(0)).build();
		attendanceRepo.saveAndFlush(attendance);
	}

	@Test
	public void findByGroupNumberAndMemberNumberAndBetweenLocalDateTimesTest() {
		for (int i = 0; i < group.size(); i++) {
			for (int j = 0; j < member.size(); j++) {
				attendanceRepo.save(Attendance.builder().status(AttendanceStatus.ABSENT).group(group.get(i))
						.member(member.get(j)).build());
			}
		}
		for (int i = 0; i < group.size(); i++) {
			for (int j = 0; j < member.size(); j++) {
				// 정상적인 상황
				Random rand = new Random();
				assertNotNull(attendanceRepo.findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(
						group.get(i).getGno(), member.get(j).getMno(),
						LocalDateTime.now().minus(rand.nextInt(15) + 1, ChronoUnit.MINUTES),
						LocalDateTime.now().plus(rand.nextInt(15) + 1, ChronoUnit.MINUTES)));
			}
		}

		for (int j = 0; j < member.size(); j++) {
			// 존재하지 않는 gno라면?
			Random rand = new Random();
			assertNull(attendanceRepo.findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(987654321L,
					member.get(j).getMno(), LocalDateTime.now().minus(rand.nextInt(15) + 1, ChronoUnit.MINUTES),
					LocalDateTime.now().plus(rand.nextInt(15) + 1, ChronoUnit.MINUTES)));
		}

		for (int i = 0; i < group.size(); i++) {
			// 존재하지 않는 mno라면?
			Random rand = new Random();
			assertNull(attendanceRepo.findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(group.get(i).getGno(),
					987654321L, LocalDateTime.now().minus(rand.nextInt(15) + 1, ChronoUnit.MINUTES),
					LocalDateTime.now().plus(rand.nextInt(15) + 1, ChronoUnit.MINUTES)));
		}

		for (int i = 0; i < group.size(); i++) {
			for (int j = 0; j < member.size(); j++) {
				// start Time 이 end Time 보다 크다면
				Random rand = new Random();
				assertNull(attendanceRepo.findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(
						group.get(i).getGno(), member.get(j).getMno(),
						LocalDateTime.now().plus(rand.nextInt(15) + 1, ChronoUnit.MINUTES),
						LocalDateTime.now().minus(rand.nextInt(15) + 1, ChronoUnit.MINUTES)));
			}
		}

		for (int i = 0; i < group.size(); i++) {
			for (int j = 0; j < member.size(); j++) {
				// 시간대가 겹치지 않는다면?
				Random rand = new Random();
				int randInt = rand.nextInt(15) + 1;
				assertNull(
						attendanceRepo.findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(group.get(i).getGno(),
								member.get(j).getMno(), LocalDateTime.now().minus(randInt + 5, ChronoUnit.MINUTES),
								LocalDateTime.now().minus(randInt, ChronoUnit.MINUTES)));
				assertNull(
						attendanceRepo.findByGroupNumberAndMemberNumberAndBetweenLocalDateTimes(group.get(i).getGno(),
								member.get(j).getMno(), LocalDateTime.now().plus(randInt, ChronoUnit.MINUTES),
								LocalDateTime.now().plus(randInt + 5, ChronoUnit.MINUTES)));
			}
		}
	}

	@Test
	public void getPageWithGroupNumberAndMemberNumberTest() {
		List<Attendance> testGroup = new ArrayList<>();
		List<Integer> testGroupSize = new ArrayList<>();
		for (int i = 0; i < group.size(); i++) {
			for (int j = 0; j < member.size(); j++) {
				Random random = new Random();
				int newSz = 10 + random.nextInt(20);
				testGroupSize.add(newSz);
				for (int k = 0; k < newSz; k++) {
					testGroup.add(Attendance.builder().status(AttendanceStatus.ABSENT).group(group.get(i))
							.member(member.get(j)).build());
				}
			}
		}
		attendanceRepo.saveAll(testGroup);
		// test Group 생성

		PageVO pageVO = new PageVO();
		PageTester<Attendance> tester = new PageTester<>();

		for (int i = 0; i < group.size(); i++) {
			for (int j = 0; j < member.size(); j++) {
				int sz = testGroupSize.get(i * member.size() + j);
				for (int pageNum = 1; pageNum <= sz / pageVO.getSize(); pageNum++) {
					pageVO.setPage(pageNum);
					Page<Attendance> page = attendanceRepo.getPageWithGroupNumberAndMemberNumber(
							pageVO.makePageable(PageDirection.DESC, "ano"), group.get(i).getGno(),
							member.get(j).getMno());
					tester.pageTest(page, testGroup, pageNum, sz, PageDirection.DESC);
				}
				int notExistPage = sz / pageVO.getSize() + 5;
				pageVO.setPage(notExistPage);
				Page<Attendance> page = attendanceRepo.getPageWithGroupNumberAndMemberNumber(
						pageVO.makePageable(PageDirection.DESC, "ano"), group.get(i).getGno(), member.get(j).getMno());
				assertEquals(page.getNumberOfElements(),0);
			}

		}

	}

	@Test
	public void findByGroupNumberAndLocalDateTest() {
		List<Attendance> testGroup = new ArrayList<>();
		for (int i = 0; i < group.size(); i++) {
			for (int j = 0; j < member.size(); j++) {
				testGroup.add(Attendance.builder().status(AttendanceStatus.ABSENT).group(group.get(i))
						.member(member.get(j)).build());
			}
		}
		attendanceRepo.saveAll(testGroup);
		// test Group 생성
		for(int i=0;i<group.size();i++) {
			assertEquals(attendanceRepo.findByGroupNumberAndLocalDate(group.get(i).getGno(), LocalDate.now()).size(),member.size());
		}
		for(int i=0;i<group.size();i++) {
			assertEquals(attendanceRepo.findByGroupNumberAndLocalDate(group.get(i).getGno(), LocalDate.now().minus(5,ChronoUnit.DAYS)).size(),0);
		}
		
		for(int i=0;i<group.size();i++) {
			assertEquals(attendanceRepo.findByGroupNumberAndLocalDate(group.get(i).getGno(), LocalDate.now().plus(5,ChronoUnit.DAYS)).size(),0);
		}
	}

	@Test
	public void updateStatusByAttendanceNumberTest() {
		Attendance attendance = Attendance.builder().status(AttendanceStatus.ABSENT).group(group.get(0)).member(member.get(0)).build();
		attendanceRepo.save(attendance);
		
		Attendance beforeUpdateAttendance = attendanceRepo.findById(attendance.getAno()).get();
		assertEquals(beforeUpdateAttendance.getStatus(),AttendanceStatus.ABSENT);
		
		attendanceRepo.updateStatusByAttendanceNumber(AttendanceStatus.LATE, attendance.getAno());
		
		Attendance afterUpdateAttendance = attendanceRepo.findById(attendance.getAno()).get();
		assertEquals(afterUpdateAttendance.getStatus(),AttendanceStatus.LATE);
	}

}
