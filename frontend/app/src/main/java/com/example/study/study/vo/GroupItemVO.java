package com.example.study.study.vo;

public class GroupItemVO {
    private Long gno;

    private String title;

    private String type;

    private String detail;

    private Long maximumNumberOfPeople;

    private Long depositToBePaid;

    private Long maximumNumberOfAbsencesAllowed;

    private Long fineForBeingLate;

    private Long fineForBeingAbsence;

    private String duedate;

    private String willRecruit;

    private String regdate;

    private String updatedate;

    private String inviteCode;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Long getGno() {
        return gno;
    }

    public void setGno(Long gno) {
        this.gno = gno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getMaximumNumberOfPeople() {
        return maximumNumberOfPeople;
    }

    public void setMaximumNumberOfPeople(Long maximumNumberOfPeople) {
        this.maximumNumberOfPeople = maximumNumberOfPeople;
    }

    public Long getDepositToBePaid() {
        return depositToBePaid;
    }

    public void setDepositToBePaid(Long depositToBePaid) {
        this.depositToBePaid = depositToBePaid;
    }

    public Long getMaximumNumberOfAbsencesAllowed() {
        return maximumNumberOfAbsencesAllowed;
    }

    public void setMaximumNumberOfAbsencesAllowed(Long maximumNumberOfAbsencesAllowed) {
        this.maximumNumberOfAbsencesAllowed = maximumNumberOfAbsencesAllowed;
    }

    public Long getFineForBeingLate() {
        return fineForBeingLate;
    }

    public void setFineForBeingLate(Long fineForBeingLate) {
        this.fineForBeingLate = fineForBeingLate;
    }

    public Long getFineForBeingAbsence() {
        return fineForBeingAbsence;
    }

    public void setFineForBeingAbsence(Long fineForBeingAbsence) {
        this.fineForBeingAbsence = fineForBeingAbsence;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getWillRecruit() {
        return willRecruit;
    }

    public void setWillRecruit(String willRecruit) {
        this.willRecruit = willRecruit;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public GroupItemVO(Long gno, String title, String type, String detail, Long maximumNumberOfPeople, Long depositToBePaid, Long maximumNumberOfAbsencesAllowed, Long fineForBeingLate, Long fineForBeingAbsence, String duedate, String willRecruit, String regdate, String updatedate) {
        this.gno = gno;
        this.title = title;
        this.type = type;
        this.detail = detail;
        this.maximumNumberOfPeople = maximumNumberOfPeople;
        this.depositToBePaid = depositToBePaid;
        this.maximumNumberOfAbsencesAllowed = maximumNumberOfAbsencesAllowed;
        this.fineForBeingLate = fineForBeingLate;
        this.fineForBeingAbsence = fineForBeingAbsence;
        this.duedate = duedate;
        this.willRecruit = willRecruit;
        this.regdate = regdate;
        this.updatedate = updatedate;
    }
}
