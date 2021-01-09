package com.untact.englishspelling.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of="spelling")
public class EnglishSpelling {
	@Id
	@Column(length=150)
	private String spelling;//영어 철자
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private EnglishSpellingDifficulty lv;//단어 난이도
}
