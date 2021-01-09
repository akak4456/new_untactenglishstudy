package com.untact.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupWaitingVO {
	public GroupWaitingVO(Long gno, Long mno) {
		this.gno = gno;
		this.mno = mno;
	}
	private Long gno;
	private Long mno;
}
