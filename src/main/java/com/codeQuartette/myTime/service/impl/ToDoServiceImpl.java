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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Transactional
    @CacheEvict(cacheNames = "find-todo", allEntries = true)
    public void create(Long userId, ToDoDTO.Request toDoRequestDTO) {
        User user = userService.findUser(userId);
        MyDate myDate = myDateService.findMyDate(user, toDoRequestDTO.getDate());
        MyDate saveMyDate = myDateService.save(myDate);
        ToDo toDo = ToDo.create(toDoRequestDTO, saveMyDate);
        toDoRepository.save(toDo);
    }

    @Override
    @CacheEvict(cacheNames = "find-todo", allEntries = true)
    public ToDo update(Long id, ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = findToDo(id);
        toDo.update(toDoRequestDTO);
        return toDoRepository.save(toDo);
    }

    //할 일 완료체크
    @Override
    @CacheEvict(cacheNames = "find-todo", allEntries = true)
    public ToDo updateDone(Long id, ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = findToDo(id);
        toDo.updateDone(toDoRequestDTO);
        return toDoRepository.save(toDo);
    }

    @Override
    @CacheEvict(cacheNames = "find-todo", allEntries = true)
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
    @Transactional
    @Cacheable(cacheNames = "find-todo", key = "#userId.toString() + ':' + #date")
    public List<ToDo> find(Long userId, LocalDate date) {
        User user = userService.findUser(userId);
        MyDate myDate = myDateService.findMyDate(user, date);
        return toDoRepository.findAllByMyDate(myDate);
    }
}
