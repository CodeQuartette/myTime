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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    private final UserService userService;

    private final MyDateService myDateService;


    private ToDo findToDo(Long toDoId) {
        return toDoRepository.findById(toDoId)
                .orElseThrow(ToDoNotFoundException::new);
    }

    @Override
    public void create(Long userId, ToDoDTO.Request toDoRequestDTO) {
        User user = userService.findUser(userId);
        MyDate myDate = myDateService.findMyDate(user, toDoRequestDTO.getDate());
        ToDo toDo = ToDo.create(toDoRequestDTO);
        myDate.addToDo(toDo);
        myDateService.save(myDate);
    }

    @Override
    public void update(Long id, ToDoDTO.Request toDoRequestDTO) {
        findToDo(id).update(toDoRequestDTO);
        toDoRepository.save(findToDo(id));
    }

    //할 일 완료체크 - 수정
    public void updateDone(Long id, ToDoDTO.Request toDoRequestDTO) {
        findToDo(id).updateDone(toDoRequestDTO);
        toDoRepository.save(findToDo(id));
    }

    @Override
    public void delete(Long id) {
        toDoRepository.delete(findToDo(id));
    }

    //한건 조회
    @Override
    public List<ToDo> find(Long id) {
        return List.of(findToDo(id));
    }

    //날짜 별 조회
    @Override
    public List<ToDo> find(Long userId, LocalDate date) {
        MyDate myDate = myDateService.find(date).stream()
                .filter(targetDate -> targetDate.matchUser(userId))
                .findFirst()
                .orElseThrow(ToDoNotFoundException::new);

        return myDate.getToDos();
    }
}
