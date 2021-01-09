package com.untact.vocabulary.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.untact.englishspelling.domain.EnglishSpelling;
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
@SequenceGenerator(name="v_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="vno")
public class Vocabulary {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="v_seq")
	private Long vno;//단어장 번호
	
	private String title;//단어장 제목
	
	private Long cnt;//갯수
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime regdate;//단어장 생성 시간
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@UpdateTimestamp
	private LocalDateTime updatedate;//단어장 수정 시간
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn
	private GroupEntity group;//이 단어장은 어떤 그룹에 속해 있나?
	public void setGroup(GroupEntity group) {
		this.group = group;
	}
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn
	private MemberEntity member;//이 단어장은 누가 썻는가?
	public void setMember(MemberEntity member) {
		this.member = member;
	}
}
