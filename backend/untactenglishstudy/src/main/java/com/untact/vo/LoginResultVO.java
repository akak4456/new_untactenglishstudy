package com.untact.vo;

import com.untact.member.domain.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultVO {
	private String token;
	private MemberEntity member;
}
