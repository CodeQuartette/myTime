package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.request.ToDoInfoRequestDTO;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.repository.ToDoRepository;
import com.codeQuartette.myTime.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    @Override
    public void create(ToDoInfoRequestDTO toDoInfoRequestDTO) {
        toDoRepository.save(ToDo.create(toDoInfoRequestDTO));
    }
}
