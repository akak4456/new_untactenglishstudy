package com.example.study.timetable.vo;

import java.util.List;

public class RepresentativeTimeTableVO {
    private String title;
    private List<TimeTableItem> timeTableItem;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TimeTableItem> getTimeTableItem() {
        return timeTableItem;
    }

    public void setTimeTableItem(List<TimeTableItem> timeTableItem) {
        this.timeTableItem = timeTableItem;
    }

    public RepresentativeTimeTableVO(){

    }

    public RepresentativeTimeTableVO(String title, List<TimeTableItem> timeTableItem) {
        this.title = title;
        this.timeTableItem = timeTableItem;
    }
}
