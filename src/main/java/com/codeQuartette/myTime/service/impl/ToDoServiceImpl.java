package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.ToDoRepository;
import com.codeQuartette.myTime.service.MyDateService;
import com.codeQuartette.myTime.service.ToDoService;
import com.codeQuartette.myTime.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    private final UserService userService;

    private final MyDateService myDateService;

    private ToDo saveToDo(ToDoDTO.Request toDoRequestDTO) {
        return toDoRepository.save(ToDo.builder()
                .title(toDoRequestDTO.getTitle())
                .color(toDoRequestDTO.getColor())
                .date(toDoRequestDTO.getDate())
                .isDone(Boolean.FALSE)
                .isBlind(toDoRequestDTO.getIsBlind())
                .build());
    }

    @Override
    @Transactional
    public void create(Long userId, ToDoDTO.Request toDoRequestDTO) {
        User user = userService.findUser(userId);
        MyDate myDate = myDateService.findMyDate(user, toDoRequestDTO.getDate());
        ToDo toDo = ToDo.create(toDoRequestDTO);
        myDate.addToDo(toDo);
        myDateService.save(myDate);
    }

    public ToDo findById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("할 일이 없어요"));
    }
}
