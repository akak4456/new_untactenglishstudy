package com.untact.persistent.util;

import java.util.List;

import org.springframework.data.domain.Page;

import com.untact.vo.PageDirection;

public class PageTester<T> {
	public boolean pageTest(
			Page<T> page,
			List<T> testGroup,
			int pageNum,
			int expectedTotalElementsCount,
			PageDirection direction) {
		List<T> result = page.getContent();
		if(page.getTotalElements() != expectedTotalElementsCount) {
			return false;
		}
		if(page.getNumber() != pageNum -1) {
			return false;
		}
		for(int cur=0;cur<testGroup.size();cur++) {
			if(result.get(0).equals(testGroup.get(cur))) {
				if(direction == PageDirection.DESC) {
					return DESCTest(result,testGroup,cur);
				}else if(direction == PageDirection.ASC) {
					return ASCTest();
				}
			}
		}
		return false;
	}
	
	private boolean DESCTest(List<T> result,List<T> testGroup,int start) {
		if(result.size() == 0)
			return false;
		if(start-result.size() < -1)
			return false;
		int resultIdx = 0;
		for(int i=start;i>start-result.size();i--) {
			if(!result.get(resultIdx).equals(testGroup.get(i))) {
				return false;
			}
			resultIdx++;
		}
		return true;
	}
	
	private boolean ASCTest() {
		return false;
	}
}
