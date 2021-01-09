package com.untact.groupinclude.persistent;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.domain.WhichStatus;

public interface GroupIncludeRepository extends GroupIncludeCustomRepository, JpaRepository<GroupInclude, Long> {
	@Modifying
	@Transactional
	@Query("update GroupInclude g set g.whichStatus = :whichStatus WHERE g.gino=:gino")
	public int updateStatusByGroupIncludeNumber(@Param("whichStatus")WhichStatus whichStatus,@Param("gino")Long gino);
	
	@Modifying
	@Transactional
	@Query("update GroupInclude g set g.whichStatus = :whichStatus WHERE g.group.gno=:gno AND g.member.mno=:mno")
	public int updateStatusByGroupNumberAndMemberNumber(@Param("whichStatus")WhichStatus whichStatus,@Param("gno")Long gno,@Param("mno")Long mno);
	
	@Query("select groupInclude from GroupInclude groupInclude where groupInclude.group.gno=:gno and groupInclude.member.mno=:mno and groupInclude.whichStatus=:whichStatus")
	public Optional<GroupInclude> findByGroupNumberAndMemberNumberAndWhichStatus(@Param("gno")Long gno,@Param("mno")Long mno,@Param("whichStatus")WhichStatus whichStatus);

	@Query("select groupInclude from GroupInclude groupInclude where groupInclude.group.gno=:gno and groupInclude.member.mno=:mno")
	public Optional<GroupInclude> findByGroupNumberAndMemberNumber(@Param("gno")Long gno,@Param("mno")Long mno);
	
	@Query("SELECT COUNT(*) FROM GroupInclude groupInclude WHERE groupInclude.group.gno=:gno AND groupInclude.whichStatus IN :statusList")
	public Long findCountByGroupNumber(@Param("gno")Long gno,@Param("statusList")Set<WhichStatus> statusList);
	
	@Query("SELECT SUM(g.fine) FROM GroupInclude g WHERE g.group.gno=:gno AND g.whichStatus IN :statusList")
	public Long findSumOfFineByGroupNumber(@Param("gno")Long gno,@Param("statusList")Set<WhichStatus>statusList);
	
	@Query("SELECT SUM(g.reward) FROM GroupInclude g WHERE g.group.gno=:gno AND g.whichStatus IN :statusList")
	public Long findSumOfRewardByGroupNumber(@Param("gno")Long gno,@Param("statusList")Set<WhichStatus>statusList);
	
	@Query("SELECT SUM(g.deposit) FROM GroupInclude g WHERE g.group.gno=:gno AND g.whichStatus IN :statusList")
	public Long findSumOfDepositByGroupNumber(@Param("gno")Long gno,@Param("statusList")Set<WhichStatus>statusList);

}
