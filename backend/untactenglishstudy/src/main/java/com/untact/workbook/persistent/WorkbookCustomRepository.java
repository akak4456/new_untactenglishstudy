package com.untact.workbook.persistent;

import java.util.List;

import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.englishspelling.domain.EnglishSpellingDifficulty;
import com.untact.workbook.domain.Workbook;
import com.untact.workbook.domain.WorkbookKind;

public interface WorkbookCustomRepository {
	public List<Workbook> findBywordListAndKind(List<EnglishSpelling> words,WorkbookKind kind);
	public List<Workbook> findByKind(WorkbookKind kind);
	public List<Workbook> findByKindAndDifficulty(WorkbookKind kind,EnglishSpellingDifficulty difficulty);
}
