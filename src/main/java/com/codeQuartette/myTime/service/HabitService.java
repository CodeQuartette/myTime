package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.HabitDTO;

public interface HabitService {

    void create(Long userId, HabitDTO.Request habitRequestDTO);

    void update(Long id, HabitDTO.Request habitRequestDTO);

    void delete(Long id);

    Habit findHabit(Long id);

    List<String> getCategory();
}
