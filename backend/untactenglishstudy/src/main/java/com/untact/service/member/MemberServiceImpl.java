package com.untact.service.member;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.untact.exception.NoMatchMemberInformationException;
import com.untact.member.domain.EmailCheck;
import com.untact.member.domain.MemberEntity;
import com.untact.member.domain.Role;
import com.untact.member.persistent.MemberEntityRepository;
import com.untact.vo.ChangeMemberInfoVO;
import com.untact.vo.ChangeMemberPasswordVO;
import com.untact.vo.MemberVO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MemberEntityRepository memberRepo;
	@Override
	@Transactional
	public Long addMember(MemberVO memberVO) {
		//보안, 입력확인등 별도의 과정 필요함
		MemberEntity entity = MemberEntity.builder()
								.email(memberVO.getEmail())
								.password(passwordEncoder.encode(memberVO.getPassword()))
								.name(memberVO.getName())
								.role(Role.MEMBER)
								.remainPoint(0L)
								.refundPoint(0L)
								.emailCheck(EmailCheck.N)
								.build();
		memberRepo.save(entity);
		return entity.getMno();
	}
	@Override
	public MemberEntity login(MemberVO memberVO) throws NoMatchMemberInformationException {
		Optional<MemberEntity> entity = memberRepo.findByEmail(memberVO.getEmail());
		if(!entity.isPresent()||!passwordEncoder.matches(memberVO.getPassword(), entity.get().getPassword())) {
			//존재하는 아이디가 없거나 비밀번호가 일치하지 않는다면
			throw new NoMatchMemberInformationException();
		}
		return entity.get();
	}
	@Override
	public void changeInfo(MemberEntity oldMember,ChangeMemberInfoVO memberVO) {
		MemberEntity newMember = oldMember.modifyInfo(memberVO);
		//이메일, 역할, 비밀번호는 변경 불가(역할은 관리자만이 바꿀 수 있음) 나머지는 바꿀수도 있음
		memberRepo.save(newMember);
	}
	@Override
	public boolean changePassword(MemberEntity oldMember, ChangeMemberPasswordVO memberVO) {
		if(!passwordEncoder.matches(memberVO.getOldPw(),oldMember.getPassword())) {
			return false;
		}
		MemberEntity newMember = oldMember.modifyPassword(passwordEncoder.encode(memberVO.getNewPw()));
		memberRepo.save(newMember);
		return true;
	}
	@Override
	public void deleteInfo(Long mno) {
		memberRepo.deleteById(mno);
	}
	@Override
	public boolean isDuplicateEmail(String email) {
		return memberRepo.findByEmail(email).isPresent();
	}
	@Override
	public void updateAuthKey(String email, String authKey) {
		memberRepo.updateAuthKeyByEmail(authKey, email);
	}
	@Override
	public void updateIsEmailCheckToTrue(String email) {
		memberRepo.updateEmailCheckByEmail(EmailCheck.Y, email);
	}
	@Override
	public void pay(MemberEntity member, Long amount) {
		member.addRemainPoint(amount);
		memberRepo.save(member);
	}
	@Override
	public boolean refund(MemberEntity member, Long amount) {
		if(member.getRemainPoint() < amount) {
			return false;
		}
		member.subRemainPoint(amount);
		memberRepo.save(member);
		return true;
	}
}
