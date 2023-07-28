package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.ScheduleHasMyDate;

import java.util.List;

public interface ScheduleHasMyDateService {

    void saveAll(List<ScheduleHasMyDate> scheduleHasMyDates);
}
