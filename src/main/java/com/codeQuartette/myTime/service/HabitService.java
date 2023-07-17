package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.HabitDTO;

public interface HabitService {

    void create(HabitDTO.Request habitRequestDTO);

    void update(Long id, HabitDTO.Request habitRequestDTO);

    void delete(Long id);
}
