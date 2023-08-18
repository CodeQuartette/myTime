package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.controller.dto.HabitHasMyDateDTO;
import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.domain.value.Category;
import com.codeQuartette.myTime.exception.HabitNotFoundException;
import com.codeQuartette.myTime.repository.HabitRepository;
import com.codeQuartette.myTime.service.HabitHasMyDateService;
import com.codeQuartette.myTime.service.HabitService;
import com.codeQuartette.myTime.service.MyDateService;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitHasMyDateRepository habitHasMyDateRepository;
    private final UserService userService;
    private final MyDateService myDateService;
    private final HabitHasMyDateService habitHasMyDateService;

    @Override
    @Transactional
    public void create(Long userId, HabitDTO.Request habitRequestDTO) {
        User user = userService.findById(userId);
        Habit habit = Habit.create(habitRequestDTO);
        List<LocalDate> allHabitDates =
                myDateService.checkAllDateByStartDateAndEndDate(habit.getStartDate(), habit.getEndDate(), habitRequestDTO.getRepeatDay());
        List<MyDate> newMyDates = allHabitDates.stream().map(date -> new MyDate(date, user)).toList();
        List<MyDate> saveMyDates = myDateService.saveAllMyDate(newMyDates);

        habit = habitRepository.save(habit);

        List<HabitHasMyDate> habitHasMyDates = new ArrayList<>();
        for(MyDate saveMyDate : saveMyDates) {
            habitHasMyDates.add(HabitHasMyDate.builder().myDate(saveMyDate).habit(habit).build());
        }
        habitHasMyDateService.saveAll(habitHasMyDates);
    }

    @Override
    public void update(Long userId, Long id, HabitDTO.Request habitRequestDTO) {
        User user = userService.findById(userId);
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("수정하려는 습관을 조회할 수 없습니다."));

        habit.update(habitRequestDTO);
        habitRepository.save(habit);
        habitHasMyDateService.deleteAllNotDone(habit.getId());

        List<LocalDate> allHabitDates =
                myDateService.checkAllDateByStartDateAndEndDate(habit.getStartDate(), habit.getEndDate(), habitRequestDTO.getRepeatDay());
        List<MyDate> newMyDates = allHabitDates.stream().map(date -> new MyDate(date, user)).toList();
        List<MyDate> saveMyDates = myDateService.saveAllMyDate(newMyDates);

        List<HabitHasMyDate> habitHasMyDates = new ArrayList<>();
        for(MyDate saveMyDate : saveMyDates) {
            habitHasMyDates.add(HabitHasMyDate.builder().myDate(saveMyDate).habit(habit).build());
        }
        habitHasMyDateService.saveAll(habitHasMyDates);
    }

    @Override
    public void delete(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("삭제하려는 습관을 조회할 수 없습니다."));
        habitRepository.delete(habit);
    }

    @Override
    public HabitDTO.Response getHabitById(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("해당 습관을 조회할 수 없습니다."));

        return HabitDTO.Response.of(habit);
    }

    @Override
    public List<HabitHasMyDateDTO.Response> getHabitByDate(LocalDate date) {
        List<HabitHasMyDate> habitHasMyDates = habitHasMyDateRepository.findAllByMyDate_DateIs(date);
        return habitHasMyDates.stream().map(habitHasMyDate -> HabitHasMyDateDTO.Response.of(habitHasMyDate)).toList();
    }

    @Override
    public List<HabitHasMyDateDTO.Response> getHabitByMonth(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<HabitHasMyDate> habitHasMyDates = habitHasMyDateRepository.findAllByMyDate_DateBetween(startDate, endDate);
        return habitHasMyDates.stream().map(habitHasMyDate -> HabitHasMyDateDTO.Response.of(habitHasMyDate)).toList();
    }

    @Override
    public List<String> getCategory() {
        return Arrays.stream(Category.values()).map(c -> c.toString()).toList();
    }
}
