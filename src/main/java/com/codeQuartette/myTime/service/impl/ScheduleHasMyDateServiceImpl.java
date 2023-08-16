package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import com.codeQuartette.myTime.repository.ScheduleHasMyDateRepository;
import com.codeQuartette.myTime.service.ScheduleHasMyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleHasMyDateServiceImpl implements ScheduleHasMyDateService {

    private final ScheduleHasMyDateRepository repository;

    @Override
    public void saveAll(List<ScheduleHasMyDate> scheduleHasMyDates) {
        repository.saveAll(scheduleHasMyDates);
    }

    @Override
    public void deleteAll(List<ScheduleHasMyDate> scheduleHasMyDates){
        repository.deleteAll(scheduleHasMyDates);
    }

    @Override
    public List<ScheduleHasMyDate> findScheduleHasMyDate(Schedule schedule) {
        return repository.findBySchedule(schedule);
    }

}
