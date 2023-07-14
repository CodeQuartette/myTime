package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.request.ToDoInfoRequestDTO;

public interface ToDoService {
    void create(ToDoInfoRequestDTO toDoInfoRequestDTO);
}
