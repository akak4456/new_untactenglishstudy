package com.untact.timetableitem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Time {
	private int hour;
	private int minute;
	public boolean isLessThan(Time target){
        if(this.hour < target.hour){
            return true;
        }else if(this.hour == target.hour){
            return this.minute < target.minute;
        }else{
            return false;
        }
    }
	public static int diff(Time start, Time end) {
		//end-start의 차이
		//단위는 분
		return (end.hour-start.hour)*60+(end.minute-start.minute);
	}
}
