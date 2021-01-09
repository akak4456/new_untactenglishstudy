package com.untact.vo;

import com.untact.group.domain.GroupEntity;
import com.untact.member.domain.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupAndMemberVO {
	private GroupEntity group;
	private MemberEntity member;
}
