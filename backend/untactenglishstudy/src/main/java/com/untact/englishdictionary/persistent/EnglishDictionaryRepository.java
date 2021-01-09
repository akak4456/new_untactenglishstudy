package com.untact.englishdictionary.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.englishdictionary.domain.EnglishDictionary;
import com.untact.workbook.domain.Workbook;

public interface EnglishDictionaryRepository extends JpaRepository<EnglishDictionary, Long>, EnglishDictionaryCustomRepository {
	@Query(value="SELECT e.* FROM english_dictionary e INNER JOIN english_spelling s ON e.spelling = s.spelling  ORDER BY RAND() LIMIT :cnt",nativeQuery = true)
	public List<EnglishDictionary> findByRandom(@Param("cnt")Long cnt);
}
