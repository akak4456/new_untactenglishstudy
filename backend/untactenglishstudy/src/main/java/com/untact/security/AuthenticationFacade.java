package com.untact.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.untact.member.domain.MemberEntity;

public class AuthenticationFacade {
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	public static MemberEntity getMemberEntityFromAuthentication() {
		return (MemberEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
