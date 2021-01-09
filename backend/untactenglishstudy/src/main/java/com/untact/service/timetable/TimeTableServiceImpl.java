package com.untact.service.timetable;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.untact.exception.NotGroupLeaderException;
import com.untact.exception.NotIncludeGroupException;
import com.untact.exception.TimeTableNotCorrectException;
import com.untact.group.domain.GroupEntity;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.domain.WhichStatus;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.representativetimetableitem.domain.RepresentativeTimeTableItem;
import com.untact.representativetimetableitem.domain.RepresentativeTimeTableItemConstant;
import com.untact.representativetimetableitem.persistent.RepresentativeTimeTableItemRepository;
import com.untact.timetable.domain.TimeTable;
import com.untact.timetable.persistent.TimeTableRepository;
import com.untact.timetableitem.domain.Time;
import com.untact.timetableitem.domain.TimeTableItem;
import com.untact.timetableitem.persistent.TimeTableItemRepository;
import com.untact.vo.PageVO;
import com.untact.vo.RepresentativeTimeTableVO;
import com.untact.vo.TimeTableVO;

@Service
public class TimeTableServiceImpl implements TimeTableService {
	/*
    시간표가 맞는지, 겹치는 스티커들(일정)은 없는지 확인
     */
	@Autowired
	private TimeTableRepository timeTableRepo;
	@Autowired
	private TimeTableItemRepository timeTableItemRepo;
	@Autowired
	private RepresentativeTimeTableItemRepository representativeTimeTableItemRepo;
	@Autowired
	private GroupEntityRepository groupRepo;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	@Override
	@Transactional
	public Page<TimeTable> getListWithPagingAndGroupNumberAndMemberNumber(PageVO pageVO, Long gno, Long mno) {
		return timeTableRepo.getPageWithGroupNumberAndMemberNumber(pageVO.makePageable(0, "tno"), gno, mno);
	}
	@Override
	@Transactional
	public TimeTableVO getOne(Long tno) {
		return new TimeTableVO(timeTableRepo.findById(tno).get(),timeTableItemRepo.findByTimeTableNumber(tno));
	}
	@Override
	@Transactional
	public void addTimeTable(Long gno,MemberEntity member,TimeTable timeTable, List<TimeTableItem> timeTableItem) throws NotIncludeGroupException,TimeTableNotCorrectException {
		groupIncludeCheck(gno,member.getMno());
		if(!verifyTimeTableItems(timeTableItem)) {
			throw new TimeTableNotCorrectException();
		}
		timeTable.setGroup(groupRepo.findById(gno).get());
		timeTable.setMember(member);
		timeTableRepo.save(timeTable);
		for(TimeTableItem item:timeTableItem) {
			item.setTimeTable(timeTable);
		}
		timeTableItemRepo.saveAll(timeTableItem);
	}
	@Override
	@Transactional
	public void modifyTimeTable(Long gno,Long mno,Long tno, TimeTable targetTable, List<TimeTableItem> targetTimeTableItem)
			throws NotIncludeGroupException,TimeTableNotCorrectException {
		groupIncludeCheck(gno,mno);
		if(!verifyTimeTableItems(targetTimeTableItem)) {
			throw new TimeTableNotCorrectException();
		}
		TimeTable oldTimeTable = timeTableRepo.findById(tno).get();
		TimeTable newTimeTable = oldTimeTable.modifyThisToTargetTimeTable(targetTable);
		timeTableRepo.save(newTimeTable);
		timeTableItemRepo.deleteByTimeTableNumber(tno);
		for(TimeTableItem item:targetTimeTableItem) {
			item.setTimeTable(newTimeTable);
		}
		timeTableItemRepo.saveAll(targetTimeTableItem);
	}
	@Override
	@Transactional
	public void deleteTimeTable(Long gno, Long mno, Long tno) throws NotIncludeGroupException {
		groupIncludeCheck(gno,mno);
		timeTableRepo.deleteById(tno);
	}
	private void groupIncludeCheck(Long gno,Long mno) throws NotIncludeGroupException {
		Optional<GroupInclude> groupIncludeOptional = groupIncludeRepo.findByGroupNumberAndMemberNumber(gno, mno);
		if(groupIncludeOptional.isEmpty()) {
			//가입 시도를 한 적이 없다면
			throw new NotIncludeGroupException();
		}
		GroupInclude groupInclude = groupIncludeOptional.get();
		if(groupInclude.getWhichStatus().equals(WhichStatus.LEADER)||groupInclude.getWhichStatus().equals(WhichStatus.FOLLOWER)) {
			//그룹을 가입한 적이 있다면
			
		}else {
			//그룹을 가입한 적이 없다면
			throw new NotIncludeGroupException();
		}
	}
	
	private boolean verifyTimeTableItems(List<TimeTableItem> timeTableItems) {
		for(int i=0;i<timeTableItems.size();i++) {
			TimeTableItem a = timeTableItems.get(i);
			Time startTime = new Time(a.getStartHour(),a.getStartMinute());
			Time endTime = new Time(a.getEndHour(),a.getEndMinute());
			if(Time.diff(startTime, endTime) < RepresentativeTimeTableItemConstant.minimun) {
				return false;
			}
			if(endTime.isLessThan(startTime)) {
				return false;
			}
			for(int j=i+1;j<timeTableItems.size();j++) {
				TimeTableItem b= timeTableItems.get(j);
				if(a.getDay() == b.getDay()&&a.isOverlap(b)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean verifyRepresentativeTimeTableItems(List<RepresentativeTimeTableItem> representativeTimeTableItems) {
		for(int i=0;i<representativeTimeTableItems.size();i++) {
			RepresentativeTimeTableItem a = representativeTimeTableItems.get(i);
			Time startTime = new Time(a.getStartHour(),a.getStartMinute());
			Time endTime = new Time(a.getEndHour(),a.getEndMinute());
			if(Time.diff(startTime, endTime) < RepresentativeTimeTableItemConstant.minimun) {
				return false;
			}
			if(endTime.isLessThan(startTime)) {
				return false;
			}
			for(int j=i+1;j<representativeTimeTableItems.size();j++) {
				RepresentativeTimeTableItem b= representativeTimeTableItems.get(j);
				if(a.getDay() == b.getDay()&&a.isOverlap(b)) {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public RepresentativeTimeTableVO getRepresentativeOne(Long gno,MemberEntity member) throws NotGroupLeaderException {
		GroupEntity group = groupRepo.findById(gno).get();
		String title = group.getRepresentativeTimeTableTitle();
		List<RepresentativeTimeTableItem> representativeTimeTableItem = null;
		if(title != null) {
			representativeTimeTableItem = representativeTimeTableItemRepo.findByGroupNumber(group.getGno());
		}
		return new RepresentativeTimeTableVO(title,representativeTimeTableItem);
	}
	@Override
	public void modifyRepresentativeTimeTable(Long gno, MemberEntity member, String title,
			List<RepresentativeTimeTableItem> targetTimeTableItem) throws NotGroupLeaderException, TimeTableNotCorrectException {
		if(groupIncludeRepo.findByGroupNumberAndMemberNumberAndWhichStatus(gno, member.getMno(), WhichStatus.LEADER).isEmpty()) {
			throw new NotGroupLeaderException();
		}
		if(!verifyRepresentativeTimeTableItems(targetTimeTableItem)) {
			throw new TimeTableNotCorrectException();
		}
		GroupEntity group = groupRepo.findById(gno).get();
		String oldTitle = group.getRepresentativeTimeTableTitle();
		GroupEntity newGroup = group.modiftyTitle(title);
		groupRepo.save(newGroup);
		representativeTimeTableItemRepo.deleteByGroupNumber(gno);
		for(RepresentativeTimeTableItem item:targetTimeTableItem) {
			item.setGroup(group);
		}
		representativeTimeTableItemRepo.saveAll(targetTimeTableItem);
	}
	@Override
	public TimeTableVO toggleAlarm(Long tno) {
		TimeTable timetable = timeTableRepo.findById(tno).get();
		List<TimeTableItem> timetableItems = timeTableItemRepo.findByTimeTableNumber(tno);
		timetable.toggleAlarm();
		timeTableRepo.save(timetable);
		return new TimeTableVO(timetable,timetableItems);
	}
	

}
