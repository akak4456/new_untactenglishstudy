package com.untact.timetableitem.persistent;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.timetableitem.domain.TimeTableItem;

public interface TimeTableItemRepository extends JpaRepository<TimeTableItem, Long>, TimeTableItemCustomRepository {
	@Modifying
	@Transactional
	@Query("DELETE FROM TimeTableItem i where i.timeTable.tno=:tno")
	public int deleteByTimeTableNumber(@Param("tno")Long tno);
	
	
	@Query("SELECT i FROM TimeTableItem i WHERE i.timeTable.tno=:tno")
	public List<TimeTableItem> findByTimeTableNumber(@Param("tno")Long tno);
	
}
