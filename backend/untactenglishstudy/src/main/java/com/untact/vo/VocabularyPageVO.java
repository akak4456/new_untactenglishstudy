package com.untact.vo;

import org.springframework.data.domain.Page;

import com.untact.vocabulary.domain.Vocabulary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyPageVO {
	private Page<Vocabulary> page;
	private Long defaultVocaCount;
}
