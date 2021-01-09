package com.untact.vo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageVO {
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final int DEFAULT_MAX_PAGE_SIZE = 500;
	private int page;
	private int size;
	public PageVO() {
		this.page = 1;
		this.size = DEFAULT_PAGE_SIZE;
	}
	public PageVO(int page) {
		this.setPage(page);
		this.size = DEFAULT_PAGE_SIZE;
	}
	public int getPage() {
		return this.page;
	}
	public void setPage(int page) {
		this.page = page >= 1? page:1;
	}
	public int getSize() {
		return this.size;
	}
	public void setSize(int size) {
		this.size = size < 0 || size > DEFAULT_MAX_PAGE_SIZE?
				DEFAULT_PAGE_SIZE:size; 
	}
	
	public Pageable makePageable(int direction, String... props) {
		Sort.Direction dir = direction == 0?Sort.Direction.DESC:Sort.Direction.ASC;
		return PageRequest.of(this.page-1,this.size, dir,props);
	}
}
