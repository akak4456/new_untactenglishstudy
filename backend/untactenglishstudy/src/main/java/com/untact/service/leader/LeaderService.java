package com.untact.service.leader;

import java.time.LocalDate;
import java.util.List;

import com.untact.attendance.vo.AttendanceVO;
import com.untact.exception.NotGroupLeaderException;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.member.domain.MemberEntity;
import com.untact.vo.MemberManageVO;

public interface LeaderService {
	public List<MemberManageVO> getListWithGroupNumber(Long gno,MemberEntity member) throws NotGroupLeaderException;
	public boolean forceExit(Long gno,MemberEntity leader,Long targetMno);
	public boolean changeReward(Long gno,MemberEntity leader,Long targetMno,Long newAmount);
	public boolean changeFine(Long gno,MemberEntity leader,Long targetMno,Long newAmount);
	
	public List<AttendanceVO> getAttendanceListWithGroupNumberAndLocalDate(Long gno,LocalDate time);
	public boolean changeAttendance(Long gno,MemberEntity leader,Long targetMno,Long ano,String oldStatus,String newStatus);
	
	public List<GroupInclude> getWaitingList(Long gno);
	public boolean rejectAll(Long gno,MemberEntity leader);
}
