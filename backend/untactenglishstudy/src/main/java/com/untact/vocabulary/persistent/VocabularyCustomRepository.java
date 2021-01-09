package com.untact.vocabulary.persistent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.untact.vocabulary.domain.Vocabulary;

public interface VocabularyCustomRepository {
	public Page<Vocabulary> getPageWithGroupNumberAndMemberNumber(Pageable pageable, Long gno,Long mno);
}
