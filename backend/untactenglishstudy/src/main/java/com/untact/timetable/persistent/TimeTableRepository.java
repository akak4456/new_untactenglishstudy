package com.untact.timetable.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.timetable.domain.TimeTable;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long>, TimeTableCustomRepository {

}
