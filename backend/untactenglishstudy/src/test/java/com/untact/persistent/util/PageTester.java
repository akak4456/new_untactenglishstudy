package com.untact.persistent.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.springframework.data.domain.Page;

import com.untact.vo.PageDirection;

public class PageTester<T> {
	
	public PageTester() {
		
	}
	public void pageTest(
			Page<T> page,
			List<T> testGroup,
			int pageNum,
			int expectedTotalElementsCount,
			PageDirection direction) {
		List<T> result = page.getContent();
		assertEquals(page.getTotalElements(),expectedTotalElementsCount);
		assertEquals(page.getNumber(),pageNum-1);
		assertNotEquals(result.size(),0);
		for(int cur=0;cur<testGroup.size();cur++) {
			if(result.get(0).equals(testGroup.get(cur))) {
				if(direction == PageDirection.DESC) {
					DESCTest(result,testGroup,cur);
				}else if(direction == PageDirection.ASC) {
					ASCTest();
				}
			}
		}
	}
	
	private void DESCTest(List<T> result,List<T> testGroup,int start) {
		if(start-result.size() < -1)
			fail();
		int resultIdx = 0;
		for(int i=start;i>start-result.size();i--) {
			if(!result.get(resultIdx).equals(testGroup.get(i))) {
				fail();
			}
			resultIdx++;
		}
	}
	
	private void ASCTest() {
		fail();
	}
}
