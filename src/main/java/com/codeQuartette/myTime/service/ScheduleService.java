package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.Schedule;

import java.time.LocalDate;
import java.time.YearMonth;

public interface ScheduleService {

    Schedule create(Long userId, ScheduleDTO.Request request);

    void delete(Long userId, Long scheduleId);

    ScheduleDTO.ResponseList find(Long scheduleId);

    ScheduleDTO.ResponseList find(Long userId, LocalDate date);

    ScheduleDTO.ResponseList find(Long userId, YearMonth yearMonth);
}
