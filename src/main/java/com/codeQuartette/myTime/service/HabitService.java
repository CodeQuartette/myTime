package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.HabitDTO;

import java.time.LocalDate;
import java.util.List;

public interface HabitService {

    void create(Long userId, HabitDTO.Request habitRequestDTO);

    void update(Long id, HabitDTO.Request habitRequestDTO);

    void delete(Long id);

    HabitDTO.Response getHabitById(Long id);

    List<HabitDTO.Response> getHabitByDate(LocalDate date);
}
