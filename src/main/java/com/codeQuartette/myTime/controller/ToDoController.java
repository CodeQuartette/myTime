package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping
    public void create(@RequestParam(name = "userId", required = false) Long userId,
                       @RequestBody ToDoDTO.Request toDoRequestDTO) {
        toDoService.create(userId, toDoRequestDTO);
    }

    @PatchMapping
    public ResponseEntity<ToDoDTO.Response> update(@RequestParam(name = "id", required = false) Long id,
                                                   @RequestBody ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = toDoService.update(id, toDoRequestDTO);
        ToDoDTO.Response response = ToDoDTO.Response.of(toDo);
        return ResponseEntity.ok(response);
    }

    //할 일 완료 체크
    @PatchMapping("/isDone")
    public ResponseEntity<ToDoDTO.Response> updateDone(@RequestParam(name = "id", required = false) Long id,
                                                       @RequestBody ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = toDoService.updateDone(id, toDoRequestDTO);
        ToDoDTO.Response response = ToDoDTO.Response.of(toDo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "id", required = false) Long id) {
        toDoService.delete(id);
    }

    //조회
    @GetMapping
    public ResponseEntity<ToDoDTO.ResponseList> find(@RequestParam(name = "toDoId", required = false) Long id,
                                                     @RequestParam(name = "userId", required = false) Long userId,
                                                     @RequestParam(name = "date", required = false) LocalDate date) {

        List<ToDo> toDos = new ArrayList<>();
        if (id != null) {
            toDos = toDoService.find(id);
        } else if (userId != null) {
            toDos = toDoService.find(userId, date);
        }

        List<ToDoDTO.Response> toDos2 = toDoService.find(userId, date)
                .stream()
                .map(ToDoDTO.Response::of)
                .toList();

        return ResponseEntity.ok(ToDoDTO.ResponseList.of(toDos2));
    }
}
