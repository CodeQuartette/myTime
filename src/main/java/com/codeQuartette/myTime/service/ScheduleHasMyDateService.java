package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.ScheduleHasMyDate;

import java.util.List;

public interface ScheduleHasMyDateService {

    void saveAll(List<ScheduleHasMyDate> scheduleHasMyDates);

    void deleteAll(List<ScheduleHasMyDate> scheduleHasMyDates);

    List<ScheduleHasMyDate> findScheduleHasMyDate(Schedule schedule);
}
