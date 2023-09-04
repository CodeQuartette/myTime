package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleHasMyDateRepository extends JpaRepository<ScheduleHasMyDate, Long>, ScheduleHasMyDateBulkRepository {

    List<ScheduleHasMyDate> findBySchedule(Schedule schedule);

}
