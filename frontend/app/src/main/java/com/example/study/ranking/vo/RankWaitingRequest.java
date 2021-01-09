package com.example.study.ranking.vo;

public class RankWaitingRequest {
    private Long first_prize;
    private Long second_prize;
    private Long third_prize;

    public RankWaitingRequest(Long first_prize, Long second_prize, Long third_prize) {
        this.first_prize = first_prize;
        this.second_prize = second_prize;
        this.third_prize = third_prize;
    }
}
