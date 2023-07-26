package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;

public interface ToDoService {
    void create(Long userId, ToDoDTO.Request toDoRequestDTO);

    ToDo findById(Long id);
}
