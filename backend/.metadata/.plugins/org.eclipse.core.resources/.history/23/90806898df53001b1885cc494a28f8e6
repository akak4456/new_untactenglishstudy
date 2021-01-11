package com.untact.persistent.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageTester<T> {
	public void pageTest(
			Page<T> page,
			List<T> testGroup,
			int pageNum,
			int expectedTotalElementsCount,
			int expectedTotalPages,
			int expectedPageSize,
			int direction) {
		List<T> result = page.getContent();
		assertEquals(page.getTotalElements(),expectedTotalElementsCount);
		assertEquals(page.getTotalPages(),expectedTotalPages);
		assertEquals(result.size(),expectedPageSize);
		assertEquals(page.getNumber(),pageNum-1);
		for(T t:testGroup) {
			if(result.get(0).equals(testGroup.get(0))) {
				return;
			}
		}
		fail();
	}
}
