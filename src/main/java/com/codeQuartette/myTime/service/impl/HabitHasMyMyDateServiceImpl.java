package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.HabitHasMyDateBulkRepository;
import com.codeQuartette.myTime.repository.HabitHasMyDateRepository;
import com.codeQuartette.myTime.service.HabitHasMyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitHasMyMyDateServiceImpl implements HabitHasMyDateService {

    private final HabitHasMyDateBulkRepository habitHasDateBulkRepository;
    private final HabitHasMyDateRepository habitHasMyDateRepository;

    public void saveAll(List<HabitHasMyDate> habitHasMyDates) {
        habitHasDateBulkRepository.saveAll(habitHasMyDates);
    }

    public void deleteAllNotDone(Long habitId) {
        habitHasDateBulkRepository.deleteAllNotDone(habitId);
    }

    public List<HabitHasMyDate> findAllHabitHasMyDate(User user, LocalDate date) {
        return habitHasMyDateRepository.findAllByMyDate_UserAndMyDate_DateIs(user, date);
    }

    public List<HabitHasMyDate> findAllHabitHasMyDate(User user, LocalDate startDate, LocalDate endDate) {
        return habitHasMyDateRepository.findAllByMyDate_UserAndMyDate_DateBetween(user, startDate, endDate);
    }
}
