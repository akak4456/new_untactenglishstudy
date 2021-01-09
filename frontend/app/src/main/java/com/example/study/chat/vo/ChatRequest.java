package com.example.study.chat.vo;

public class ChatRequest {
    private Long mno;
    private String from;
    private String msg;

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

    public ChatRequest(Long mno, String from, String msg) {
        this.mno = mno;
        this.from = from;
        this.msg = msg;
    }
    public ChatRequest(){

    }
}
