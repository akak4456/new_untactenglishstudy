package com.untact.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberManageVO {
	private Long mno;
	private String name;
	private Long attendance;
	private Long absent;
	private Long late;
	private Long deposit;
	private Long fine;
	private Long reward;
}
