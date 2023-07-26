package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import com.codeQuartette.myTime.repository.ScheduleHasMyDateBulkRepository;
import com.codeQuartette.myTime.service.ScheduleHasMyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleHasMyDateServiceImpl implements ScheduleHasMyDateService {

    private final ScheduleHasMyDateBulkRepository repository;

    @Override
    public void saveAll(List<ScheduleHasMyDate> scheduleHasMyDates) {
        repository.saveAll(scheduleHasMyDates);
    }
}
