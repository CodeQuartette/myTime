package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleSelectRepository {
}
