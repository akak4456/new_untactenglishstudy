package com.untact.service.group;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.untact.group.domain.GroupEntity;
import com.untact.member.domain.MemberEntity;
import com.untact.vo.GroupInfoVO;
import com.untact.vo.PageVO;
import com.untact.vo.RewardAndFineVO;

public interface GroupService {
	public Page<GroupEntity> getListWithPaging(PageVO pageVO);
	public Page<GroupEntity> getListWithPagingAndUserNumber(PageVO pageVO,Long mno);
	public GroupInfoVO getOne(Long gno);
	public String tryEntrance(Long gno,MemberEntity member);
	public void addGroup(GroupEntity group,MemberEntity member);
	public boolean dismissGroupManual(Long gno,MemberEntity member);
	public void dismissGroupAuto(LocalDateTime duedate);//cron에 의해 주기적으로 실행되는 함수
	public boolean tryLeaderEntrance(Long gno,MemberEntity member);
	public boolean modifyGroup(Long gno,MemberEntity member,GroupEntity newGroup);
	public Long getGroupMemberCount(Long gno);
	public RewardAndFineVO getRewardAndFine(Long gno,MemberEntity member);
}
