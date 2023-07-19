package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleHasMyDateRepository extends JpaRepository<ScheduleHasMyDate, Long> {

    Optional<ScheduleHasMyDate> findBySchedule(Schedule schedule);
}
