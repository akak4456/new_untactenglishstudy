package com.untact.vo;

import com.untact.group.domain.GroupEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoVO {
	private GroupEntity group;
	private Long memberCount;
}
