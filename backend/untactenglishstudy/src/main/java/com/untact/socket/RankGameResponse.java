package com.untact.socket;

import lombok.Getter;

@Getter
public class RankGameResponse {
	private String kind;
	
	private ProblemSet problems;
	
	private String first_member;
	
	private String second_member;
	
	private String third_member;
	
	private Long first_prize;
	
	private Long second_prize;
	
	private Long third_prize;
	
	public RankGameResponse(String kind,ProblemSet problems) {
		this.kind = kind;
		this.problems = problems;
	}
	
	public RankGameResponse(String kind,String first_member,String second_member, String third_member,Long first_prize,Long second_prize,Long third_prize) {
		this.kind = kind;
		this.first_member = first_member;
		this.second_member = second_member;
		this.third_member = third_member;
		this.first_prize = first_prize;
		this.second_prize = second_prize;
		this.third_prize = third_prize;
	}
	public RankGameResponse(String kind) {
		this.kind = kind;
	}
}
