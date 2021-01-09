package com.untact.group.persistent;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.untact.demo.UntactenglishstudyApplication;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class GenerateGroup {
	@Autowired
	private GroupEntityRepository groupRepo;
	private static final int MAX_ENTITY_COUNT = 105;
	@Test
	public void generateGroup() {
		generateGroupList();
	}
	private List<GroupEntity> generateGroupList(){
		List<GroupEntity> list = new ArrayList<>();
		for(int i=0;i<MAX_ENTITY_COUNT;i++) {
			GroupEntity entity = generateGroupEntity("title"+i,"detail"+i);
			list.add(entity);
		}
		groupRepo.saveAll(list);
		return list;
	}
	private GroupEntity generateGroupEntity(String title,String detail) {
		return new GroupEntity().builder().title(title).detail(detail).build();
	}
}
