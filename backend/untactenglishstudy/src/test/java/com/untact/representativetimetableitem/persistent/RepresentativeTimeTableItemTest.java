package com.untact.representativetimetableitem.persistent;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
import com.untact.persistent.util.DeleteAllUtil;
import com.untact.representativetimetableitem.domain.RepresentativeTimeTableItem;
import com.untact.representativetimetableitem.persistent.RepresentativeTimeTableItemRepository;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class RepresentativeTimeTableItemTest {
	@Autowired
	private DeleteAllUtil deleteAllUtil;
	
	@Autowired
	private RepresentativeTimeTableItemRepository representativeTimeTableItemRepo;
	
	@Autowired
	private GroupEntityRepository groupRepo;
	
	@Before
	public void setUp() {
		deleteAllUtil.deleteAllRepo();
	}
	
	@Test
	public void initTest() {
		GroupEntity group1 = GroupEntity.builder().title("title1").representativeTimeTableTitle("time1").build();
		groupRepo.save(group1);
		GroupEntity group2 = GroupEntity.builder().title("title2").representativeTimeTableTitle("time2").build();
		groupRepo.save(group2);
		List<RepresentativeTimeTableItem> list1 = new ArrayList<>();
		list1.add(RepresentativeTimeTableItem.builder()
				.title("title1")
				.detail("detail1")
				.day(0)
				.startHour(10)
				.startMinute(0)
				.endHour(12)
				.endMinute(0)
				.group(group1)
				.build());
		list1.add(RepresentativeTimeTableItem.builder()
				.title("title2")
				.detail("detail2")
				.day(0)
				.startHour(11)
				.startMinute(0)
				.endHour(13)
				.endMinute(0)
				.group(group1)
				.build());
		list1.add(RepresentativeTimeTableItem.builder()
				.title("title3")
				.detail("detail3")
				.day(0)
				.startHour(13)
				.startMinute(0)
				.endHour(15)
				.endMinute(0)
				.group(group1)
				.build());
		list1.add(RepresentativeTimeTableItem.builder()
				.title("title4")
				.detail("detail4")
				.day(1)
				.startHour(10)
				.startMinute(0)
				.endHour(12)
				.endMinute(0)
				.group(group1)
				.build());
		list1.add(RepresentativeTimeTableItem.builder()
				.title("title5")
				.detail("detail5")
				.day(1)
				.startHour(11)
				.startMinute(0)
				.endHour(13)
				.endMinute(0)
				.group(group1)
				.build());
		list1.add(RepresentativeTimeTableItem.builder()
				.title("title6")
				.detail("detail6")
				.day(1)
				.startHour(13)
				.startMinute(0)
				.endHour(15)
				.endMinute(0)
				.group(group1)
				.build());
		representativeTimeTableItemRepo.saveAll(list1);
		List<RepresentativeTimeTableItem> list2 = new ArrayList<>();
		list2.add(RepresentativeTimeTableItem.builder()
				.title("title1")
				.detail("detail1")
				.day(0)
				.startHour(10)
				.startMinute(0)
				.endHour(12)
				.endMinute(0)
				.group(group2)
				.build());
		list2.add(RepresentativeTimeTableItem.builder()
				.title("title2")
				.detail("detail2")
				.day(0)
				.startHour(11)
				.startMinute(0)
				.endHour(13)
				.endMinute(0)
				.group(group2)
				.build());
		list2.add(RepresentativeTimeTableItem.builder()
				.title("title3")
				.detail("detail3")
				.day(0)
				.startHour(13)
				.startMinute(0)
				.endHour(15)
				.endMinute(0)
				.group(group2)
				.build());
		list2.add(RepresentativeTimeTableItem.builder()
				.title("title4")
				.detail("detail4")
				.day(1)
				.startHour(10)
				.startMinute(0)
				.endHour(12)
				.endMinute(0)
				.group(group2)
				.build());
		list2.add(RepresentativeTimeTableItem.builder()
				.title("title5")
				.detail("detail5")
				.day(1)
				.startHour(11)
				.startMinute(0)
				.endHour(13)
				.endMinute(0)
				.group(group2)
				.build());
		list2.add(RepresentativeTimeTableItem.builder()
				.title("title6")
				.detail("detail6")
				.day(1)
				.startHour(13)
				.startMinute(0)
				.endHour(15)
				.endMinute(0)
				.group(group2)
				.build());
		representativeTimeTableItemRepo.saveAll(list2);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(0, 10, 0).size(),2);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(0, 10, 1).size(),0);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(0, 11, 0).size(),2);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(0, 11, 1).size(),0);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(0, 13, 0).size(),2);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(0, 13, 1).size(),0);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(1, 10, 0).size(),2);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(1, 10, 1).size(),0);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(1, 11, 0).size(),2);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(1, 11, 1).size(),0);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(1, 13, 0).size(),2);
		assertEquals(representativeTimeTableItemRepo.findGroupNumberByDayAndStartTime(1, 13, 1).size(),0);
	}
}
