package com.example.study.global;

public class Invite {
    private static Invite instance = null;
    private Long gno;
    private String inviteCode;

    private Invite(){
        gno = 0L;
        inviteCode = "";
    }

    public static synchronized  Invite getInstance(){
        if(instance == null){
            instance = new Invite();
        }
        return instance;
    }

    public Long getGno() {
        return gno;
    }

    public void setGno(Long gno) {
        this.gno = gno;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
