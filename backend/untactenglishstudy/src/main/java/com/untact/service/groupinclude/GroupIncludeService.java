package com.untact.service.groupinclude;

import com.untact.member.domain.MemberEntity;
import com.untact.vo.GroupWaitingVO;

public interface GroupIncludeService {
	public void groupWithdraw(Long gno,MemberEntity memberWantToWithdraw);
	public void groupEject(Long gno,MemberEntity leaderMember,MemberEntity memberWantToWithdraw);
	public boolean requestJoin(GroupWaitingVO groupWaitingVO);
	public boolean acceptJoin(Long gno,Long gino,MemberEntity leader);
	public boolean rejectJoin(Long gno,Long gino,MemberEntity leader);
	
	public boolean depositPay(Long gno,MemberEntity member);
	
	public boolean isLeader(Long gno,MemberEntity member);
}
