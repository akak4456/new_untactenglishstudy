package com.untact.socket;

import java.time.LocalDateTime;
import java.util.List;

import com.untact.vo.QuizVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSet {
	private LocalDateTime startTime;
	
	private List<Problem> problems;
}
