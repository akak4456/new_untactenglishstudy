package com.untact.vocabulary.persistent;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.vocabulary.domain.Vocabulary;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long>, VocabularyCustomRepository {
	@Modifying
	@Transactional
	@Query("UPDATE Vocabulary v SET v.cnt=:cnt  where v.vno=:vno")
	void updateCntByVocabularyNumber(@Param("cnt")Long cnt,@Param("vno")Long vno);
}
