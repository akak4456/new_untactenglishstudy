package com.untact.member.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sun.istack.NotNull;
import com.untact.vo.ChangeMemberInfoVO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@SequenceGenerator(name="m_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="mno")
public class MemberEntity implements UserDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="m_seq")
	private Long mno;//멤버 번호
	
	@Column(unique=true)
	private String email;//이메일 주소
	
	private String password;//비밀 번호
	
	private String name;//사용자 이름
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private Role role;//사용자인가 관리자인가?
	
	private Long remainPoint;
	
	private Long refundPoint;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private EmailCheck emailCheck;
	
	private String authKey;
	
	public void addRemainPoint(Long addPoint) {
		this.remainPoint += addPoint;
	}
	
	public void subRemainPoint(Long subPoint) {
		this.remainPoint -= subPoint;
	}
	public MemberEntity copy() {
		//비밀번호 빼고 나머지
		return MemberEntity.builder()
							.mno(this.mno)
							.email(this.email)
							.name(this.name)
							.role(this.role)
							.remainPoint(this.remainPoint)
							.refundPoint(this.refundPoint)
							.emailCheck(this.emailCheck)
							.build();
	}
	
	public MemberEntity updateRemainPoint(Long newRemainPoint) {
		this.remainPoint = newRemainPoint;
		return this;
	}
	
	public MemberEntity modifyInfo(ChangeMemberInfoVO vo) {
		this.name = vo.getName();
		return this;
	}
	
	public MemberEntity modifyPassword(String encodedPassword) {
		this.password = encodedPassword;
		return this;
	}
	
	public void addRefundPoint(Long amount) {
		this.refundPoint += amount;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
		authority.add(new SimpleGrantedAuthority(this.role.toString()));
		return authority;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
