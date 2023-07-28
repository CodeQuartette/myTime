package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


}
