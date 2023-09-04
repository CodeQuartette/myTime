package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.Schedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface ScheduleSelectRepository {

    List<Schedule> findByDate(Long userId, LocalDate date);

    List<Schedule> findByYearMonth(Long userId, YearMonth yearMonth);
}
