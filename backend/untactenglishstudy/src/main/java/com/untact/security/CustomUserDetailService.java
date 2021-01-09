package com.untact.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.untact.member.persistent.MemberEntityRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	@Autowired
	private MemberEntityRepository memberRepo;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return memberRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
	}

}
