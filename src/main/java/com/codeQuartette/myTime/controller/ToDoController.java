package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;
}
