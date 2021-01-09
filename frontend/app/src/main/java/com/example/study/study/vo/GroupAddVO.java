package com.example.study.study.vo;

public class GroupAddVO {
    private String title;
    private String type;
    private String detail;
    private Long maximumNumberOfPeople;
    private Long depositToBePaid;
    private Long maximumNumberOfAbsencesAllowed;
    private Long fineForBeingLate;
    private Long fineForBeingAbsence;
    private String duedate;

    public GroupAddVO(String title, String type, String detail, Long maximumNumberOfPeople, Long depositToBePaid, Long maximumNumberOfAbsencesAllowed, Long fineForBeingLate, Long fineForBeingAbsence, String duedate) {
        this.title = title;
        this.type = type;
        this.detail = detail;
        this.maximumNumberOfPeople = maximumNumberOfPeople;
        this.depositToBePaid = depositToBePaid;
        this.maximumNumberOfAbsencesAllowed = maximumNumberOfAbsencesAllowed;
        this.fineForBeingLate = fineForBeingLate;
        this.fineForBeingAbsence = fineForBeingAbsence;
        this.duedate = duedate;
    }
}
