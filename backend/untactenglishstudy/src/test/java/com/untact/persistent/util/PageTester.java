package com.untact.persistent.util;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.springframework.data.domain.Page;

import com.untact.vo.PageDirection;

import jdk.internal.org.jline.utils.Log;

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
					if(!DESCTest(result,testGroup,cur)) {
						return false;
					}else {
						return true;
					}
				}else if(direction == PageDirection.ASC) {
					if(!ASCTest()) {
						return false;
					}else {
						return true;
					}
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
