package com.untact.timetableitem.persistent;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.demo.UntactenglishstudyApplication;
import com.untact.persistent.util.DeleteAllUtil;
import com.untact.timetable.domain.TimeTable;
import com.untact.timetable.persistent.TimeTableRepository;
import com.untact.timetableitem.domain.TimeTableItem;
import com.untact.timetableitem.persistent.TimeTableItemRepository;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class TimeTableItemRepositoryTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	@Autowired
	private TimeTableRepository timeTableRepo;
	@Autowired
	private TimeTableItemRepository timeTableItemRepo;
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
	}
	@Test
	public void initTest() {
	}
	@Test
	public void findByTimeTableNumberTest() {
		TimeTable time1 = TimeTable.builder().title("title1").build();
		timeTableRepo.save(time1);
		TimeTable time2 = TimeTable.builder().title("title2").build();
		timeTableRepo.save(time2);
		List<TimeTableItem> item1 = List.of(
				TimeTableItem.builder().title("title1").timeTable(time1).build(),
				TimeTableItem.builder().title("title2").timeTable(time1).build(),
				TimeTableItem.builder().title("title3").timeTable(time1).build()
				);
		timeTableItemRepo.saveAll(item1);
		List<TimeTableItem> item2 = List.of(
				TimeTableItem.builder().title("title1").timeTable(time2).build(),
				TimeTableItem.builder().title("title2").timeTable(time2).build(),
				TimeTableItem.builder().title("title3").timeTable(time2).build(),
				TimeTableItem.builder().title("title4").timeTable(time2).build(),
				TimeTableItem.builder().title("title5").timeTable(time2).build()
				);
		timeTableItemRepo.saveAll(item2);
		assertEquals(timeTableItemRepo.findByTimeTableNumber(time1.getTno()).size(),3);
		assertEquals(timeTableItemRepo.findByTimeTableNumber(time2.getTno()).size(),5);
		timeTableItemRepo.deleteByTimeTableNumber(time1.getTno());
		assertEquals(timeTableItemRepo.count(),5L);
	}
}
