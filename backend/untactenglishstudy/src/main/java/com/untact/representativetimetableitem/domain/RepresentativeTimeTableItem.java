package com.untact.representativetimetableitem.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.untact.group.domain.GroupEntity;
import com.untact.timetableitem.domain.Time;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@SequenceGenerator(name="rti_seq", initialValue=1, allocationSize=1)
@EqualsAndHashCode(of="rtino")
public class RepresentativeTimeTableItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO,generator="rti_seq")
	private Long rtino;//시간표 번호
	
	private String title;
	
	private String detail;
	
	private int day;
	
	private int startHour;
	
	private int startMinute;
	
	private int endHour;
	
	private int endMinute;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="gno")
	private GroupEntity group;//그룹과의 관계
	public void setGroup (GroupEntity group) {
		this.group = group;
	}
	public boolean isOverlap(RepresentativeTimeTableItem b) {
		Time tStartA = new Time(this.startHour,this.startMinute);
		Time tEndA = new Time(this.endHour,this.endMinute);
		Time tStartB = new Time(b.startHour,b.startMinute);
		Time tEndB = new Time(b.endHour,b.endMinute);
        return tStartA.isLessThan(tEndB)&&tStartB.isLessThan(tEndA);
	}
}
