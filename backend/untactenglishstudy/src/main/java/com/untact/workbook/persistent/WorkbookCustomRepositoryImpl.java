package com.untact.workbook.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.JPQLQuery;
import com.untact.englishspelling.domain.EnglishSpelling;
import com.untact.englishspelling.domain.EnglishSpellingDifficulty;
import com.untact.englishspelling.domain.QEnglishSpelling;
import com.untact.workbook.domain.QWorkbook;
import com.untact.workbook.domain.Workbook;
import com.untact.workbook.domain.WorkbookKind;

public class WorkbookCustomRepositoryImpl extends QuerydslRepositorySupport implements WorkbookCustomRepository {
	public WorkbookCustomRepositoryImpl() {
		super(Workbook.class);
	}

	@Override
	public List<Workbook> findBywordListAndKind(List<EnglishSpelling> words, WorkbookKind kind) {
		QWorkbook workbook = QWorkbook.workbook;
		QEnglishSpelling spelling = QEnglishSpelling.englishSpelling;
		JPQLQuery<Workbook> query = from(workbook);
		query.innerJoin(workbook.englishSpelling,spelling);
		query.fetchJoin();
		query.where(workbook.englishSpelling.in(words).and(workbook.kind.eq(kind)));
		return query.fetch();
	}

	@Override
	public List<Workbook> findByKind(WorkbookKind kind) {
		QWorkbook workbook = QWorkbook.workbook;
		QEnglishSpelling spelling = QEnglishSpelling.englishSpelling;
		JPQLQuery<Workbook> query = from(workbook);
		query.innerJoin(workbook.englishSpelling,spelling);
		query.fetchJoin();
		query.where(workbook.kind.eq(kind));
		return query.fetch();
	}

	@Override
	public List<Workbook> findByKindAndDifficulty(WorkbookKind kind, EnglishSpellingDifficulty difficulty) {
		// TODO Auto-generated method stub
		QWorkbook workbook = QWorkbook.workbook;
		QEnglishSpelling spelling = QEnglishSpelling.englishSpelling;
		JPQLQuery<Workbook> query = from(workbook);
		query.innerJoin(workbook.englishSpelling,spelling);
		query.fetchJoin();
		query.where(workbook.kind.eq(kind).and(workbook.englishSpelling.lv.eq(difficulty)));
		return query.fetch();
	}
}
