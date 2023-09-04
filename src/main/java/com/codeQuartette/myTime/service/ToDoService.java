package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;

import java.time.LocalDate;
import java.util.List;

public interface ToDoService {

    void create(Long userId, ToDoDTO.Request toDoRequestDTO);

    void update(Long id, ToDoDTO.Request toDoRequestDTO);

    void updateDone(Long id, ToDoDTO.Request isDone);

    void delete(Long id);

    List<ToDo> find(Long id);

    List<ToDo> find(Long userId, LocalDate date);
}
