package com.untact.service.vocabulary;

import java.util.List;

import org.springframework.data.domain.Page;

import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.member.domain.MemberEntity;
import com.untact.vo.PageVO;
import com.untact.vo.VocabularyPageVO;

public interface VocabularyService {
	public VocabularyPageVO getListWithPagingAndGroupNumberAndMemberNumber(PageVO pageVO,Long gno,Long mno);
	public Page<EnglishDictionary> getVocabularyItemsWithPagingAndVocabularyNumber(PageVO pageVO,Long vno);
	public boolean addVoca(Long gno,MemberEntity member,String title,List<String> content);
}
