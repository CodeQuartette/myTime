package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.HabitHasMyDateRepository;
import com.codeQuartette.myTime.repository.impl.HabitHasMyDateBulkRepository;
import com.codeQuartette.myTime.service.HabitHasMyDateService;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitHasMyDateServiceImpl implements HabitHasMyDateService {

    private final HabitHasMyDateBulkRepository habitHasDateBulkRepository;
    private final HabitHasMyDateRepository habitHasMyDateRepository;
    private final UserService userService;

    public void saveAll(List<HabitHasMyDate> habitHasMyDates) {
        habitHasDateBulkRepository.saveAll(habitHasMyDates);
    }

    public void deleteAllNotDone(Long habitId) {
        habitHasDateBulkRepository.deleteAllNotDone(habitId);
    }

    @Override
    public List<HabitHasMyDate> findAllHabitHasMyDate(Long userId, LocalDate date) {
        User user = userService.findUser(userId);
        return habitHasMyDateRepository.findAllByMyDate_UserAndMyDate_DateIs(user, date);
    }

    @Override
    public List<HabitHasMyDate> findAllHabitHasMyDate(Long userId, YearMonth yearMonth) {
        User user = userService.findUser(userId);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return habitHasMyDateRepository.findAllByMyDate_UserAndMyDate_DateBetween(user, startDate, endDate);
    }
}
