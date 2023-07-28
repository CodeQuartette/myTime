package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.Schedule;

public interface ScheduleService {

    Schedule create(Long userId, ScheduleDTO.Request request);

    void delete(Long userId, Long scheduleId);
}
