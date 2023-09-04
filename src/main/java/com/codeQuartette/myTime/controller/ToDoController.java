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
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping("/todo")
    public void create(@RequestParam(name = "userId", required = false) Long userId,
                       @RequestBody ToDoDTO.Request toDoRequestDTO) {
        toDoService.create(userId, toDoRequestDTO);
    }

    @PatchMapping("/todo")
    public void update(@RequestParam(name = "id", required = false) Long id,
                       @RequestBody ToDoDTO.Request toDoRequestDTO) {
        toDoService.update(id, toDoRequestDTO);
    }

    //할 일 완료 체크
    @PatchMapping("/todo/isDone")
    public void updateDone(@RequestParam(name = "id", required = false) Long id,
                           @RequestBody ToDoDTO.Request toDoRequestDTO) {
        toDoService.updateDone(id, toDoRequestDTO);
    }

    @DeleteMapping("/todo")
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
