package com.example.study.chat.vo;

import com.example.study.member.vo.MemberEntity;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Set;

public class ChatResponse {
    private Long mno;
    private String from;
    private String msg;
    private String time;
    private List<MemberEntity> userList;
    public Long getMno() {
        return mno;
    }

    public void setMno(Long mno) {
        this.mno = mno;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public ChatResponse(){

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<MemberEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<MemberEntity> userList) {
        this.userList = userList;
    }
}
