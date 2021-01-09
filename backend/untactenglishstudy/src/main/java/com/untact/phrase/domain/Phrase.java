package com.untact.phrase.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.reply.domain.Reply;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "verb", "prep"}))
@SequenceGenerator(name="p_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="pno")
public class Phrase {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="p_seq")
	private Long pno;
	@ManyToOne
	@JoinColumn(name="verb")
	private EnglishSpelling englishSpelling;
	public void setEnglishSpelling(EnglishSpelling englishSpelling) {
		this.englishSpelling = englishSpelling;
	}
	private String prep;
	private String meaning;
}
