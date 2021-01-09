package com.untact.workbook.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.sun.istack.NotNull;
import com.untact.englishspelling.domain.EnglishSpelling;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of="wbno")
public class Workbook {
	@Id
	@GenericGenerator(
            name = "wb_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "hibernate_sequence"),
                    @Parameter(name = "optimizer", value = "pooled"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1000")
            }
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "wb_seq"
    )
	private Long wbno;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="spelling")
	private EnglishSpelling englishSpelling;
	public void setEnglishSpelling(EnglishSpelling englishSpelling) {
		this.englishSpelling = englishSpelling;
	}
	
	private String question;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private WorkbookKind kind;
	
	private String ans1;
	
	private String ans2;
	
	private String ans3;
	
	private String ans4;
}
