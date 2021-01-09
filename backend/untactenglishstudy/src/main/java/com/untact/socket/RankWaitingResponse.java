package com.untact.socket;

import java.util.List;

import com.untact.member.domain.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class RankWaitingResponse {
	private String kind;
	private List<MemberEntity> userList;
	private Long totalPeople;//그룹 내에 있는 총원 수
	private Long targetMno;
	private String isAccept;
	public RankWaitingResponse(String kind,List<MemberEntity>userList,Long totalPeople) {
		this.kind = kind;
		this.userList = userList;
		this.totalPeople = totalPeople;
	}
	public RankWaitingResponse(String kind,Long targetMno,String isAccept) {
		this.kind = kind;
		this.targetMno = targetMno;
		this.isAccept = isAccept;
	}
}
