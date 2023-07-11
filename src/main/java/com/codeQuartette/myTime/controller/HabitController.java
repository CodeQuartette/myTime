package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;
}
