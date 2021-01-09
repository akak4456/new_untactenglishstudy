package com.example.study.timetable.vo;

import java.util.List;

public class TimeTableVO {
    private TimeTable timeTable;
    private List<TimeTableItem> timeTableItem;

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public List<TimeTableItem> getTimeTableItem() {
        return timeTableItem;
    }

    public void setTimeTableItem(List<TimeTableItem> timeTableItem) {
        this.timeTableItem = timeTableItem;
    }
    public TimeTableVO(){

    }
    public TimeTableVO(TimeTable timeTable, List<TimeTableItem> timeTableItem) {
        this.timeTable = timeTable;
        this.timeTableItem = timeTableItem;
    }
}
