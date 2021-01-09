package com.untact.representativetimetableitem.persistent;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.representativetimetableitem.domain.RepresentativeTimeTableItem;

public interface RepresentativeTimeTableItemRepository
		extends JpaRepository<RepresentativeTimeTableItem, Long>, RepresentativeTimeTableItemCustomRepository {
	@Modifying
	@Transactional
	@Query("DELETE FROM RepresentativeTimeTableItem i where i.group.gno=:gno")
	public int deleteByGroupNumber(@Param("gno")Long gno);
	
	
	@Query("SELECT i FROM RepresentativeTimeTableItem i WHERE i.group.gno=:gno")
	public List<RepresentativeTimeTableItem> findByGroupNumber(@Param("gno")Long gno);
}
