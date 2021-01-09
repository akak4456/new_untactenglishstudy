package com.untact.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
	private String kind;
	private String question;
	private String ans1;
	private String ans2;
	private String ans3;
	private String ans4;
	private String spelling;
}
