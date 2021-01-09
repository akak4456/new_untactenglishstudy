package com.untact.attendance.persistent;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.antonym.domain.Antonym;
import com.untact.attendance.domain.Attendance;
import com.untact.attendance.domain.AttendanceStatus;
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
}