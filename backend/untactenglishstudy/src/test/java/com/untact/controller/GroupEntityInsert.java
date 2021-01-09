package com.untact.controller;

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
import com.untact.service.group.GroupService;

import lombok.extern.java.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UntactenglishstudyApplication.class)
@Transactional
@Commit
@Log
public class GroupEntityInsert {
	@Autowired
	private GroupEntityRepository repo;
	@Autowired
	private GroupService groupService;
	
	@Test
	public void insertEntity() {
		repo.deleteAllInBatch();
		List<GroupEntity> list = new ArrayList<>();
		for(int i=0;i<200;i++) {
			GroupEntity entity = GroupEntity.builder().title("title"+i).build();
		}
	}
}
