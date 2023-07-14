package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.request.ToDoInfoRequestDTO;
import com.codeQuartette.myTime.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping("/todo")
    public void create(@RequestBody ToDoInfoRequestDTO toDoInfoRequestDTO){
        toDoService.create(toDoInfoRequestDTO);
    }
}
