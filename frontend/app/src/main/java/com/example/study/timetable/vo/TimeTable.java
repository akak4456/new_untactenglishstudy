package com.example.study.timetable.vo;

public class TimeTable {
    private Long tno;
    private String title;
    private String isalarm;

    public Long getTno() {
        return tno;
    }

    public void setTno(Long tno) {
        this.tno = tno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsAlarm() {
        return isalarm;
    }

    public void setIsAlarm(String isAlarm) {
        this.isalarm = isAlarm;
    }
}
