package com.untact.group.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@SequenceGenerator(name="g_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="gno")
public class GroupEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="g_seq")
	private Long gno;//그룹 번호
	
	private String title;//그룹명
	
	private String type;//시험 종류
	
	private String detail;//그룹 설명
	
	private Long maximumNumberOfPeople;//최대 인원수
	
	private Long depositToBePaid;//내야 하는 예치금
	
	private Long maximumNumberOfAbsencesAllowed;//최대 결석 횟수
	
	private Long fineForBeingLate;//지각할 때 벌금
	
	private Long fineForBeingAbsence;//결석할 때 벌금
	
	private String representativeTimeTableTitle;//대표 시간표 제목
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime duedate;//그룹 기한(그룹이 언제 끝날 것인지)
	
	private String inviteCode;//초대 코드
	
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime regdate;//그룹 생성 시간
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@UpdateTimestamp
	private LocalDateTime updatedate;//그룹 수정 시간
	
	public GroupEntity modiftyTitle(String representativeTimeTableTitle) {
		this.representativeTimeTableTitle = representativeTimeTableTitle;
		return this;
	}
	
	public void modifyGroup(GroupEntity newGroup) {
		this.title = newGroup.title;
		this.type = newGroup.type;
		this.detail = newGroup.detail;
		this.maximumNumberOfPeople = newGroup.maximumNumberOfPeople;
		this.depositToBePaid = newGroup.depositToBePaid;
		this.maximumNumberOfAbsencesAllowed = newGroup.maximumNumberOfAbsencesAllowed;
		this.fineForBeingLate = newGroup.fineForBeingLate;
		this.fineForBeingAbsence = newGroup.fineForBeingAbsence;
		this.duedate = newGroup.duedate;
	}
	
}
