package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.controller.dto.HabitHasMyDateDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface HabitService {

    void create(Long userId, HabitDTO.Request habitRequestDTO);

    void update(Long userId, Long id, HabitDTO.Request habitRequestDTO);

    void delete(Long id);

    HabitDTO.Response findHabit(Long id);

    List<HabitHasMyDateDTO.Response> findAllHabit(Long userId, LocalDate date);

    List<HabitHasMyDateDTO.Response> findAllHabit(Long userId, YearMonth yearMonth);

    List<String> getCategory();
}
