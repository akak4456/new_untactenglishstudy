package com.example.study.global;

import com.example.study.member.vo.MemberEntity;

public class MyMemberInfo {
    private static MyMemberInfo instance = null;
    private MemberEntity member;
    private MyMemberInfo(){
    }
    public static MyMemberInfo getInstance(){
        if(instance == null){
            instance = new MyMemberInfo();
        }
        return instance;
    }
    public void setMember(MemberEntity member){
        this.member = member;
    }
    public MemberEntity getMember(){
        return this.member;
    }
}
