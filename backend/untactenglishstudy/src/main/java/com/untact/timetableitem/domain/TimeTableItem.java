package com.untact.timetableitem.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.untact.timetable.domain.TimeTable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@SequenceGenerator(name="ti_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="tino")
public class TimeTableItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="ti_seq")
	private Long tino;//시간표 아이템 번호
	
	private String title;
	
	private String detail;
	
	private int day;
	
	private int startHour;
	
	private int startMinute;
	
	private int endHour;
	
	private int endMinute;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="tno")
	private TimeTable timeTable;
	public void setTimeTable (TimeTable timeTable) {
		this.timeTable = timeTable;
	}
	public boolean isOverlap(TimeTableItem b) {
		Time tStartA = new Time(this.startHour,this.startMinute);
		Time tEndA = new Time(this.endHour,this.endMinute);
		Time tStartB = new Time(b.startHour,b.startMinute);
		Time tEndB = new Time(b.endHour,b.endMinute);
        return tStartA.isLessThan(tEndB)&&tStartB.isLessThan(tEndA);
	}
}
