package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.Schedule;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ScheduleService {

    Schedule create(Long userId, ScheduleDTO.Request request);

    void delete(Long userId, Long scheduleId);

    List<Schedule> find(Long scheduleId);

    List<Schedule> find(Long userId, LocalDate date);

    List<Schedule> find(Long userId, YearMonth yearMonth);

    Schedule update(Long userId, Long scheduleId, ScheduleDTO.Request request);
}
