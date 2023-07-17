package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.exception.HabitNotFoundException;
import com.codeQuartette.myTime.repository.HabitRepository;
import com.codeQuartette.myTime.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;

    @Override
    public void create(HabitDTO.Request habitRequestDTO) {
        habitRepository.save(Habit.create(habitRequestDTO));
    }

    @Override
    public void update(Long id, HabitDTO.Request habitRequestDTO) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("수정하려는 습관을 조회할 수 없습니다."));

        habit.update(habitRequestDTO);
        habitRepository.save(habit);
    }
}
