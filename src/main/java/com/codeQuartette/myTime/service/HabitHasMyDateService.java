package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.User;

import java.time.LocalDate;
import java.util.List;

public interface HabitHasMyDateService {

    public void saveAll(List<HabitHasMyDate> habitHasMyDates);

    public void deleteAllNotDone(Long habitId);

    public List<HabitHasMyDate> findAllByMyDateAndUser(User user, LocalDate date);

    public List<HabitHasMyDate> findAllByUserAndStartDateAndEndDate(User user, LocalDate startDate, LocalDate endDate);
}
