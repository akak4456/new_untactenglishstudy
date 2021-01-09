package com.untact.englishspelling.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.englishspelling.domain.EnglishSpelling;

public interface EnglishSpellingRepository
		extends JpaRepository<EnglishSpelling, String>, EnglishSpellingCustomRepository {
	@Query("SELECT spell FROM EnglishSpelling spell WHERE spell.spelling in :newSpellings")
	public List<EnglishSpelling> findByTargetSpellingList(@Param("newSpellings")List<String> targetSpellings);

}
