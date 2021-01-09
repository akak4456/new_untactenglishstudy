package com.untact.persistent.util;


public class Range {
	private int start;
	private int end;
	private Range(int start,int end) {
		this.start = start;
		this.end = end;
	}
	public static Range of(int start,int end) {
		return new Range(start,end);
	}
	public int getStart() {
		return this.start;
	}
	public int getEnd() {
		return this.end;
	}
}
