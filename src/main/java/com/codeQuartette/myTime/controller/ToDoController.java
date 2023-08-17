package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping("/todo")
    public void create(@RequestParam Long userId, @RequestBody ToDoDTO.Request toDoRequestDTO){
        toDoService.create(userId, toDoRequestDTO);
    }

    @PatchMapping("/todo")
    public void update(@RequestParam Long id, @RequestBody ToDoDTO.Request toDoRequestDTO) {
        toDoService.update(id, toDoRequestDTO);
    }

    @DeleteMapping("/todo")
    public void delete(@RequestParam Long id) {
        toDoService.delete(id);
    }

    //한건 조회
    @GetMapping("/todo")
    public ToDoDTO.Response getToDoById(@RequestParam Long id) {
        return toDoService.getToDoById(id);
    }

    //날짜별 조회
    @GetMapping("/toDos")
    public List<ToDoDTO.Response> getToDoByDate(@RequestParam(name = "userId", required = false) Long userId,
                                                @RequestParam(name = "date", required = false) LocalDate date) {
        return toDoService.getToDoByDate(userId, date);
    }
}
