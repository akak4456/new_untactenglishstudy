package com.example.study.member.vo;

public class LoginResultVO {
    private String token;
    private MemberEntity member;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public LoginResultVO(){

    }

    public LoginResultVO(String token, MemberEntity member) {
        this.token = token;
        this.member = member;
    }
}
