package com.example.study.member.vo;

public class ChangePwVO {
    private String email;
    private String oldPw;
    private String newPw;

    public ChangePwVO(String email, String oldPw, String newPw) {
        this.email = email;
        this.oldPw = oldPw;
        this.newPw = newPw;
    }
}
