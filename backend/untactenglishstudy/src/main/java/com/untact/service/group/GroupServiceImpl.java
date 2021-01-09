package com.untact.service.group;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.untact.controller.TryEntranceResult;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.domain.WhichStatus;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.vo.GroupInfoVO;
import com.untact.vo.PageVO;
import com.untact.vo.RewardAndFineVO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
@Service
@NoArgsConstructor
@AllArgsConstructor
@Log
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupEntityRepository groupRepo;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	@Autowired
	private MemberEntityRepository memberRepo;
	
	@Transactional
	@Override
	public Page<GroupEntity> getListWithPaging(PageVO pageVO) {
		return groupRepo.getPage(pageVO.makePageable(0, "gno"));
	}
	
	@Transactional
	@Override
	public void addGroup(GroupEntity group,MemberEntity member) {
		// group add를 신청한 member는 자동으로 group leader가 된다.
		group.setInviteCode(RandomStringUtils.randomNumeric(6));
		groupRepo.save(group);
		groupIncludeRepo.save(GroupInclude.builder()
									.group(group)
									.member(member)
									.whichStatus(WhichStatus.LEADER)
									.deposit(0L)
									.fine(0L)
									.reward(0L)
									.attendance(0L)
									.absent(0L)
									.late(0L)
									.build());
	}

	@Transactional
	@Override
	public Page<GroupEntity> getListWithPagingAndUserNumber(PageVO pageVO, Long mno) {
		return groupRepo.getPageWithUserNumber(pageVO.makePageable(0, "gno"), mno);
	}
	
	private void dismissGroup(Long gno) {
		GroupEntity group = groupRepo.findById(gno).get();
		List<GroupInclude> includes = groupIncludeRepo.findByGroupNumber(gno);
		int n = includes.size();//총 사람 수
		Long totalFine = groupIncludeRepo.findSumOfFineByGroupNumber(gno, Set.of(WhichStatus.LEADER,WhichStatus.FOLLOWER));
		Long totalReward = groupIncludeRepo.findSumOfRewardByGroupNumber(gno, Set.of(WhichStatus.LEADER,WhichStatus.FOLLOWER));
		Long totalDepositForEjectedPeople = groupIncludeRepo.findSumOfDepositByGroupNumber(gno, Set.of(WhichStatus.EJECT));
		if(totalDepositForEjectedPeople == null) {
			totalDepositForEjectedPeople = 0L;
		}
		Long nDiv = (totalDepositForEjectedPeople+totalFine-totalReward)/n;
		List<MemberEntity> members = new ArrayList<>();
		for(GroupInclude inc:includes) {
			MemberEntity mem = inc.getMember();
			mem.addRefundPoint(inc.getDeposit()-inc.getFine()+inc.getReward()+nDiv);
			members.add(mem);
		}
		memberRepo.saveAll(members);//환급금을 돌려 준다.
		groupRepo.deleteById(gno);//그룹을 삭제한다.
	}
	@Transactional
	@Override
	public boolean dismissGroupManual(Long gno, MemberEntity member) {
		Optional<GroupInclude> include = groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(gno, member.getMno(), WhichStatus.LEADER);
		if(include.isEmpty()) {
			//리더가 아니라면
			return false;
		}
		dismissGroup(gno);
		return true;
	}
	
	@Transactional
	@Override
	public void dismissGroupAuto(LocalDateTime duedate) {
		List<Long> gnoList = groupRepo.findGroupNumberByDuedate(duedate);
		for(Long gno:gnoList) {
			dismissGroup(gno);
		}
	}

	@Override
	public String tryEntrance(Long gno, MemberEntity member) {
		Optional<GroupInclude> groupIncludeOptional = groupIncludeRepo.findByGroupNumberAndMemberNumber(gno, member.getMno());
		if(groupIncludeOptional.isEmpty()) {
			//가입 시도를 한 적이 없다면
			return TryEntranceResult.first.toString();
		}
		GroupInclude groupInclude = groupIncludeOptional.get();
		if(groupInclude.getWhichStatus().equals(WhichStatus.LEADER)||groupInclude.getWhichStatus().equals(WhichStatus.FOLLOWER)) {
			//그룹을 가입한 적이 있다면
			return TryEntranceResult.success.toString();
		}
		if(groupInclude.getWhichStatus().equals(WhichStatus.WAITING)) {
			return TryEntranceResult.wait.toString();
		}
		GroupEntity group = groupRepo.findById(gno).get();
		if(groupIncludeRepo.findCountByGroupNumber(gno, Set.of(WhichStatus.LEADER,WhichStatus.FOLLOWER)) >= group.getMaximumNumberOfPeople()) {
			return TryEntranceResult.full.toString();
		}
		return TryEntranceResult.denied.toString();
	}

	@Override
	public GroupInfoVO getOne(Long gno) {
		GroupEntity group = groupRepo.findById(gno).get();
		Long memberCount = groupIncludeRepo.findCountByGroupNumber(gno,EnumSet.of(WhichStatus.LEADER,WhichStatus.FOLLOWER));
		return new GroupInfoVO(group,memberCount);
	}

	@Override
	public boolean tryLeaderEntrance(Long gno, MemberEntity member) {
		return groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(gno, member.getMno(), WhichStatus.LEADER).isPresent();
	}

	@Override
	public boolean modifyGroup(Long gno, MemberEntity member,GroupEntity newGroup) {
		if(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(gno, member.getMno(), WhichStatus.LEADER).isEmpty())
			return false;
		GroupEntity group = groupRepo.findById(gno).get();
		group.modifyGroup(newGroup);
		groupRepo.save(group);
		return true;
	}

	@Override
	public Long getGroupMemberCount(Long gno) {
		return groupIncludeRepo.findCountByGroupNumber(gno, Set.of(WhichStatus.LEADER,WhichStatus.FOLLOWER));
	}

	@Override
	public RewardAndFineVO getRewardAndFine(Long gno, MemberEntity member) {
		GroupInclude include = groupIncludeRepo.findByGroupNumberAndMemberNumber(gno,member.getMno()).get();
		return new RewardAndFineVO(include.getReward(),include.getFine());
	}

}
