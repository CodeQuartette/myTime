package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.ScheduleHasMyDate;

import java.util.List;

public interface ScheduleHasMyDateBulkRepository {

    void saveAll(List<ScheduleHasMyDate> scheduleHasMyDates);

    void deleteAll(List<ScheduleHasMyDate> scheduleHasMyDates);
}
