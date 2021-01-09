package com.untact.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
@Getter
public class PageMaker <T> {
	private Page<T> result;
	
	private Pageable prevPage;
	private Pageable nextPage;
	
	private int currentPageNumber;
	private Pageable currentPage;
	
	private int totalPages;
	
	private List<Pageable> pageList;
	
	private boolean accessible;
	
	private PageVO pageVO;
	
	public PageMaker(Page<T> result,PageVO pageVO) {
		this.result = result;
		this.currentPage = result.getPageable();
		this.currentPageNumber = currentPage.getPageNumber() + 1;
		this.totalPages = result.getTotalPages();
		this.pageList = new ArrayList<>();
		this.prevPage = null;
		this.nextPage = null;
		this.accessible = currentPageNumber <= totalPages;
		this.pageVO = pageVO;
		calcPage();
	}
	private void calcPage() {
		int rangeNum = (this.currentPageNumber % pageVO.getSize() == 0?this.currentPageNumber/pageVO.getSize()-1
							:this.currentPageNumber/pageVO.getSize());
		int startNum = rangeNum * pageVO.getSize() + 1;
		int endNum = (rangeNum+1)*pageVO.getSize();
		Pageable startPage = this.currentPage;
		for(int i = startNum; i < this.currentPageNumber;i++) {
			startPage = startPage.previousOrFirst();
		}
		if(rangeNum > 0) {
			this.prevPage = startPage.previousOrFirst();
		}
		for(int i=startNum;i<=endNum;i++) {
			this.pageList.add(startPage);
			startPage = startPage.next();
		}
		if(startPage.getPageNumber() < totalPages) {
			this.nextPage = startPage;
		}
	}
}
