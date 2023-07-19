package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import com.codeQuartette.myTime.repository.ScheduleHasMyDateRepository;
import com.codeQuartette.myTime.service.ScheduleHasMyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleHasMyDateServiceImpl implements ScheduleHasMyDateService {

    private final ScheduleHasMyDateRepository scheduleHasMyDateRepository;

    @Override
    public ScheduleHasMyDate save(ScheduleHasMyDate scheduleHasMyDate) {
        return scheduleHasMyDateRepository.save(scheduleHasMyDate);
    }
}
