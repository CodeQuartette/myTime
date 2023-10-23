package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseType;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.service.ToDoService;
import lombok.RequiredArgsConstructor;
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
    public ResponseDTO<String> create(@RequestParam(name = "userId", required = false) Long userId,
                       @RequestBody ToDoDTO.Request toDoRequestDTO) {
        toDoService.create(userId, toDoRequestDTO);
        return  ResponseDTO.from(ResponseType.CREATED);
    }

    @PatchMapping
    public ResponseDTO<ToDoDTO.Response> update(@RequestParam(name = "id", required = false) Long id,
                                                @RequestBody ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = toDoService.update(id, toDoRequestDTO);
        ToDoDTO.Response response = ToDoDTO.Response.of(toDo);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    //할 일 완료 체크
    @PatchMapping("/isDone")
    public ResponseDTO<ToDoDTO.Response> updateDone(@RequestParam(name = "id", required = false) Long id,
                                                       @RequestBody ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = toDoService.updateDone(id, toDoRequestDTO);
        ToDoDTO.Response response = ToDoDTO.Response.of(toDo);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @DeleteMapping
    public ResponseDTO<String> delete(@RequestParam(name = "id", required = false) Long id) {
        toDoService.delete(id);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }

    //조회
    @GetMapping
    public ResponseDTO<ToDoDTO.ResponseList> find(@RequestParam(name = "id", required = false) Long id,
                                                     @RequestParam(name = "userId", required = false) Long userId,
                                                     @RequestParam(name = "date", required = false) LocalDate date) {

        List<ToDo> toDos = new ArrayList<>();
        if (id != null) {
            toDos = toDoService.find(id);
        } else if (userId != null) {
            toDos = toDoService.find(userId, date);
        }

        List<ToDoDTO.Response> toDos2 = toDos
                .stream()
                .map(ToDoDTO.Response::of)
                .toList();

        return ResponseDTO.from(ResponseType.SUCCESS, ToDoDTO.ResponseList.of(toDos2));
    }
}
