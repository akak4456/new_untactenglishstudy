package com.example.study.ranking.vo;

public class RankGameResponse {
    private String kind;

    private ProblemSet problems;

    private String first_member;

    private String second_member;

    private String third_member;

    private Long first_prize;

    private Long second_prize;

    private Long third_prize;

    public String getKind() {
        return kind;
    }

    public ProblemSet getProblems() {
        return problems;
    }

    public String getFirst_member() {
        return first_member;
    }

    public String getSecond_member() {
        return second_member;
    }

    public String getThird_member() {
        return third_member;
    }

    public Long getFirst_prize() {
        return first_prize;
    }

    public Long getSecond_prize() {
        return second_prize;
    }

    public Long getThird_prize() {
        return third_prize;
    }
}
