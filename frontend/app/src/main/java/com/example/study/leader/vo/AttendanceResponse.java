package com.example.study.leader.vo;

import java.util.List;

public class AttendanceResponse {
    List<AttendanceVO> attendances;

    public List<AttendanceVO> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<AttendanceVO> attendances) {
        this.attendances = attendances;
    }
}
