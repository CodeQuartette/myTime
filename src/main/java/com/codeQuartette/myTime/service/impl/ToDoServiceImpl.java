package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.exception.ToDoNotFoundException;
import com.codeQuartette.myTime.repository.ToDoRepository;
import com.codeQuartette.myTime.service.MyDateService;
import com.codeQuartette.myTime.service.ToDoService;
import com.codeQuartette.myTime.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    private final UserService userService;

    private final MyDateService myDateService;


    @Override
    @Transactional
    public void create(Long userId, ToDoDTO.Request toDoRequestDTO) {
        User user = userService.findById(userId);
        MyDate myDate = myDateService.findMyDate(user, toDoRequestDTO.getDate());
        ToDo toDo = ToDo.create(toDoRequestDTO);
        myDate.addToDo(toDo);
        myDateService.save(myDate);
    }
    @Override
    @Transactional
    public void update(Long id, ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = toDoRepository.findById(id)
                .orElseThrow(ToDoNotFoundException::new);

        toDo.update(toDoRequestDTO);
        toDoRepository.save(toDo);
    }

    @Override
    public void delete(Long id) {
        ToDo toDo = toDoRepository.findById(id)
                .orElseThrow(ToDoNotFoundException::new);
        toDoRepository.delete(toDo);
    }
}
