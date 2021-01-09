package com.untact.groupinclude.persistent;

import java.util.List;

import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.domain.WhichStatus;
import com.untact.vo.MemberManageVO;

public interface GroupIncludeCustomRepository {
	public List<GroupInclude> findByGroupNumber(Long gno);
	public List<GroupInclude> findByGroupNumbers(List<Long> gno);
	public List<MemberManageVO> findMemberManageByGroupNumber(Long gno);
	public List<GroupInclude> findByGroupNumberAndWhichStatus(Long gno,WhichStatus whichStatus);
	public void rejectAllGroup(Long gno);
} 
