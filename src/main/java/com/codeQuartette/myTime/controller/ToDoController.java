package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.annotation.UserId;
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
    public ResponseDTO<String> create(@UserId Long userId,
                       @RequestBody ToDoDTO.Request toDoRequestDTO) {
        toDoService.create(userId, toDoRequestDTO);
        return  ResponseDTO.from(ResponseType.CREATED);
    }

    @PatchMapping
    public ResponseDTO<ToDoDTO.Response> update(@UserId Long userId,
                                                @RequestParam(name = "id", required = false) Long id,
                                                @RequestBody ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = toDoService.update(userId, id, toDoRequestDTO);
        ToDoDTO.Response response = ToDoDTO.Response.of(toDo);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    //할 일 완료 체크
    @PatchMapping("/isDone")
    public ResponseDTO<ToDoDTO.Response> updateDone(@UserId Long userId,
                                                    @RequestParam(name = "id", required = false) Long id,
                                                    @RequestBody ToDoDTO.Request toDoRequestDTO) {
        ToDo toDo = toDoService.updateDone(userId, id, toDoRequestDTO);
        ToDoDTO.Response response = ToDoDTO.Response.of(toDo);
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @DeleteMapping
    public ResponseDTO<String> delete(@UserId Long userId,
                                      @RequestParam(name = "id", required = false) Long id) {
        toDoService.delete(userId, id);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }

    //조회
    @GetMapping
    public ResponseDTO<ToDoDTO.ResponseList> find(@UserId Long userId,
                                                  @RequestParam(name = "id", required = false) Long id,
                                                  @RequestParam(name = "date", required = false) LocalDate date) {

        List<ToDo> toDos = new ArrayList<>();
        if (id != null) {
            toDos = toDoService.find(userId, id);
        } else if (date != null) {
            toDos = toDoService.find(userId, date);
        }

        List<ToDoDTO.Response> toDos2 = toDos
                .stream()
                .map(ToDoDTO.Response::of)
                .toList();

        return ResponseDTO.from(ResponseType.SUCCESS, ToDoDTO.ResponseList.of(toDos2));
    }
}
