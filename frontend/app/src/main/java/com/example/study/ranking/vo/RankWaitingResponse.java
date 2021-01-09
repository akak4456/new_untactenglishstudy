package com.example.study.ranking.vo;

import com.example.study.member.vo.MemberEntity;

import java.util.List;

public class RankWaitingResponse {
    private String kind;
    private List<MemberEntity> userList;
    private Long totalPeople;
    private Long targetMno;
    private String isAccept;
    public String getKind() {
        return kind;
    }

    public List<MemberEntity> getUserList() {
        return userList;
    }

    public Long getTotalPeople() {
        return totalPeople;
    }

    public Long getTargetMno() {
        return targetMno;
    }

    public String getIsAccept() {
        return isAccept;
    }
}
