package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.HabitHasMyDate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface HabitHasMyDateService {

    void saveAll(List<HabitHasMyDate> habitHasMyDates);

    void deleteAllNotDone(Long habitId);

    List<HabitHasMyDate> findAllHabitHasMyDate(Long userId, LocalDate date);

    List<HabitHasMyDate> findAllHabitHasMyDate(Long userId, YearMonth yearMonth);
}
