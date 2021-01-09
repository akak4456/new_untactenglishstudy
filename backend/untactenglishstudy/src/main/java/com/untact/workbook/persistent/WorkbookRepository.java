package com.untact.workbook.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.workbook.domain.Workbook;

public interface WorkbookRepository extends JpaRepository<Workbook, Long>, WorkbookCustomRepository {
	@Query(value="SELECT w.* FROM workbook w INNER JOIN english_spelling s ON w.spelling = s.spelling  ORDER BY RAND() LIMIT :cnt",nativeQuery = true)
	public List<Workbook> findByRandom(@Param("cnt")Long cnt);
}
