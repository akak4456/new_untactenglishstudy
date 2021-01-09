package com.untact.groupinclude.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.sun.istack.NotNull;
import com.untact.group.domain.GroupEntity;
import com.untact.member.domain.MemberEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "gno", "mno" }))
@SequenceGenerator(name="gi_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="gino")
public class GroupInclude {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="gi_seq")
	private Long gino;//그룹 포함 번호
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private WhichStatus whichStatus;
	
	private Long deposit;//예치금
	
	private Long fine;//벌금
	
	private Long reward;//상금
	
	private Long attendance;//출석 횟수
	
	private Long absent;//결석 횟수
	
	private Long late;//지각 횟수
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="gno")
	private GroupEntity group;//그룹 포함과 그룹과의 관계
	public void setGroup(GroupEntity group) {
		this.group = group;
	}
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="mno")
	private MemberEntity member;//사용자와 그룹 포함과의 관계
	public void setMember(MemberEntity member) {
		this.member = member;
	}
	
	public void addDeposit(Long deposit) {
		this.deposit += deposit;
	}
	public void addFine(Long fine) {
		this.fine += fine;
	}
	
	public void subFine(Long fine) {
		this.fine -= fine;
	}
	public void addReward(Long reward) {
		this.reward += reward;
	}
	
	public void incrementAbsent() {
		this.absent++;
	}
	
	public void changeAttendanceToLate() {
		this.attendance--;
		this.late++;
	}
	
	public void changeAttendanceToAbsent() {
		this.attendance--;
		this.absent++;
	}
	
	public void changeLateToAttendance() {
		this.late--;
		this.attendance++;
	}
	
	public void changeLateToAbsent() {
		this.late--;
		this.absent++;
	}
	
	public void changeAbsentToAttendance() {
		this.absent--;
		this.attendance++;
	}
	
	public void changeAbsentToLate() {
		this.absent--;
		this.late++;
	}
}
