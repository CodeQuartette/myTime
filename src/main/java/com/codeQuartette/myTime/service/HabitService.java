package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.domain.Habit;

import java.util.List;

public interface HabitService {

    void create(Long userId, HabitDTO.Request habitRequestDTO);

    void update(Long userId, Long id, HabitDTO.Request habitRequestDTO);

    void delete(Long userId, Long id);

    Habit findHabit(Long userId, Long id);

    List<String> getCategory();
}
