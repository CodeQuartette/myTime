package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;

public interface ToDoService {
    void create(Long userId, ToDoDTO.Request toDoRequestDTO);

    void update(Long id, ToDoDTO.Request toDoRequestDTO);
}
