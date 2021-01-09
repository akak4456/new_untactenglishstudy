package com.untact.service.invite;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.domain.WhichStatus;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;

@Service
public class InviteServiceImpl implements InviteService {
	@Autowired
	private GroupEntityRepository groupRepo;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	
	@Override
	public boolean inviteAccept(Long gno, MemberEntity member, String inviteCode) {
		if(groupIncludeRepo.findByGroupNumberAndMemberNumber(gno, member.getMno()).isPresent()) {
			return false;
		}
		Optional<GroupEntity> group = groupRepo.findById(gno);
		if(group.isEmpty()) {
			return false;
		}
		if(!group.get().getInviteCode().equals(inviteCode)) {
			return false;
		}
		groupIncludeRepo.save(groupIncludeRepo.save(GroupInclude.builder()
				.group(group.get())
				.member(member)
				.whichStatus(WhichStatus.FOLLOWER)
				.deposit(0L)
				.fine(0L)
				.reward(0L)
				.attendance(0L)
				.absent(0L)
				.late(0L)
				.build()));//그룹에 가입시켜준다
		return true;
	}

}
