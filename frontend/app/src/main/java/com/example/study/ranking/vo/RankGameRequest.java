package com.example.study.ranking.vo;

import java.util.List;

public class RankGameRequest {
    private List<String> userAnswer;

    public RankGameRequest(List<String> userAnswer) {
        this.userAnswer = userAnswer;
    }
}
