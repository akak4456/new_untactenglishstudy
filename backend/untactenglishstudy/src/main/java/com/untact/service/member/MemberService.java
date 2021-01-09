package com.untact.service.member;

import com.untact.exception.NoMatchMemberInformationException;
import com.untact.member.domain.MemberEntity;
import com.untact.vo.ChangeMemberInfoVO;
import com.untact.vo.ChangeMemberPasswordVO;
import com.untact.vo.MemberVO;

public interface MemberService {
	public Long addMember(MemberVO memberVO);
	public MemberEntity login(MemberVO memberVO) throws NoMatchMemberInformationException;
	public void changeInfo(MemberEntity oldMember,ChangeMemberInfoVO memberVO);
	public boolean changePassword(MemberEntity oldMember,ChangeMemberPasswordVO memberVO);
	public void deleteInfo(Long mno);
	public boolean isDuplicateEmail(String email);
	public void updateAuthKey(String email,String authKey);
	public void updateIsEmailCheckToTrue(String email);
	public void pay(MemberEntity member,Long amount);
	public boolean refund(MemberEntity member,Long amount);
}
