package com.untact.group.persistent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.untact.group.domain.GroupEntity;

public interface GroupEntityCustomRepository {
	public Page<GroupEntity> getPage(Pageable pageable);
	
	public Page<GroupEntity> getPageWithUserNumber(Pageable pageable, Long mno);
}
