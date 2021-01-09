package com.example.study.leader.vo;

import com.example.study.member.vo.MemberEntity;

public class GroupInclude {
    private Long gino;
    private MemberEntity member;

    public Long getGino() {
        return gino;
    }

    public void setGino(Long gino) {
        this.gino = gino;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }
}
