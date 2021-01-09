package com.untact.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlVO {
	private String url;
	public UrlVO(String url) {
		this.url = url;
	}
}
