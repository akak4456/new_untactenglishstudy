package com.untact.timetable.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.sun.istack.NotNull;
import com.untact.group.domain.GroupEntity;
import com.untact.member.domain.MemberEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@SequenceGenerator(name="t_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="tno")
public class TimeTable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="t_seq")
	private Long tno;//시간표 번호
	
	private String title;//시간표 제목
	private String isalarm;//알람을 울릴 것인지
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="gno")
	private GroupEntity group;//그룹과의 관계
	public void setGroup (GroupEntity group) {
		this.group = group;
	}
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="mno")
	private MemberEntity member;//사용자와의 관계
	public void setMember(MemberEntity member) {
		this.member = member;	
	}
	
	public TimeTable modifyThisToTargetTimeTable(TimeTable targetTimeTable) {
		this.title = targetTimeTable.title;
		return this;
	}
	
	public void toggleAlarm() {
		if(this.isalarm.equals("N")) {
			this.isalarm = "Y";
		}else {
			this.isalarm = "N";
		}
	}
}
