package com.untact.persistent.util;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.springframework.data.domain.Page;

import com.untact.vo.PageDirection;

public class PageTester<T> {
	public void pageTest(
			Page<T> page,
			List<T> testGroup,
			int pageNum,
			int expectedTotalElementsCount,
			PageDirection direction) {
		List<T> result = page.getContent();
		assertEquals(page.getTotalElements(),expectedTotalElementsCount);
		assertEquals(page.getNumber(),pageNum-1);
		for(T t:testGroup) {
			if(result.get(0).equals(t)) {
				return;
			}
		}
		fail();
	}
}
