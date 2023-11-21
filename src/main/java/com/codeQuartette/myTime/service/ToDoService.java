package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;

import java.time.LocalDate;
import java.util.List;

public interface ToDoService {

    ToDo create(Long userId, ToDoDTO.Request toDoRequestDTO);

    ToDo update(Long userId, Long id, ToDoDTO.Request toDoRequestDTO);

    ToDo updateDone(Long userId, Long id, ToDoDTO.Request isDone);

    void delete(Long userId, Long id);

    List<ToDo> find(Long userId, Long id);

    List<ToDo> find(Long userId, LocalDate date);
}
