package com.untact.vo;

import java.util.List;

import com.untact.timetable.domain.TimeTable;
import com.untact.timetableitem.domain.TimeTableItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableVO {
	private TimeTable timeTable;
	private List<TimeTableItem> timeTableItem;
}
