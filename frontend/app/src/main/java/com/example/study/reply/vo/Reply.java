package com.example.study.reply.vo;

import com.example.study.member.vo.MemberEntity;

public class Reply {
    private Long rno;
    private String message;
    private MemberEntity member;
    private String updatedate;

    public Reply(String message){
        this.message = message;
    }

    public Long getRno() {
        return rno;
    }

    public void setRno(Long rno) {
        this.rno = rno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }
}
