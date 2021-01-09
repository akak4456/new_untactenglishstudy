package com.untact.attendance.persistent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.antonym.domain.Antonym;
import com.untact.attendance.domain.Attendance;
import com.untact.attendance.domain.AttendanceStatus;
import com.untact.attendance.vo.AttendanceVO;
import com.untact.demo.UntactenglishstudyApplication;
import com.untact.group.domain.GroupEntity;
import com.untact.member.domain.MemberEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
public class AttendanceRepositoryTest {
	/*
	 * spring.jpa.hibernate.ddl-auto=create-drop
	 * 로 설정해서 테스트 할 것
	 */
	
	@Autowired
	private AttendanceRepository attendanceRepo;
	
	private GroupEntity group;
	
	private MemberEntity member;
	@Before
	public void setUp() {
		group = GroupEntity.builder().title("title").build();
		member = MemberEntity.builder().email("email@email.com").build();
	}
	
	@Test
	public void successSaveTest() {
		Attendance attendance = Attendance.builder()
									.status(AttendanceStatus.ABSENT)
									.group(group)
									.member(member)
									.build();
		attendanceRepo.saveAndFlush(attendance);
	}
	@Test(expected=DataIntegrityViolationException.class)
	public void statusNullSaveTest() {
		Attendance attendance = Attendance.builder()
				.group(group)
				.member(member)
				.build();
		attendanceRepo.saveAndFlush(attendance);
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void groupNullSaveTest() {
		Attendance attendance = Attendance.builder()
									.status(AttendanceStatus.ABSENT)
									.member(member)
									.build();
		attendanceRepo.saveAndFlush(attendance);
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void memberNullSaveTest() {
		Attendance attendance = Attendance.builder()
									.status(AttendanceStatus.ABSENT)
									.group(group)
									.build();
		attendanceRepo.saveAndFlush(attendance);
	}
	
	@Test
	public void findByGroupNumberAndMemberNumberAndBetweenLocalDateTimesTest() {
		
	}
	
	@Test
	public void getPageWithGroupNumberAndMemberNumberTest() {
		
	}
	
	@Test
	public void findByGroupNumberAndLocalDateTest() {
		
	}
	
	@Test
	public void updateStatusByAttendanceNumberTest() {
		
	}
	
}
