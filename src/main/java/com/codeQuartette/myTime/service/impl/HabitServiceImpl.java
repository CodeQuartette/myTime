package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.exception.HabitNotFoundException;
import com.codeQuartette.myTime.repository.HabitRepository;
import com.codeQuartette.myTime.service.HabitService;
import com.codeQuartette.myTime.service.MyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final MyDateService myDateService;

    @Override
    public void create(Long userId, HabitDTO.Request habitRequestDTO) {
        Habit habit = Habit.create(habitRequestDTO);
        List<LocalDate> allHabitDates =
                myDateService.checkAllDateByStartDateAndEndDate(habit.getStartDate(), habit.getEndDate(), habitRequestDTO.getRepeatDay());
        List<MyDate> newMyDates = myDateService.validateDates(userId, allHabitDates);
        myDateService.saveAll(newMyDates);
        habitRepository.save(Habit.create(habitRequestDTO));
    }

    @Override
    public void update(Long id, HabitDTO.Request habitRequestDTO) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("수정하려는 습관을 조회할 수 없습니다."));

        habit.update(habitRequestDTO);
        habitRepository.save(habit);
    }

    @Override
    public void delete(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("삭제하려는 습관을 조회할 수 없습니다."));
        habitRepository.delete(habit);
    }
}
