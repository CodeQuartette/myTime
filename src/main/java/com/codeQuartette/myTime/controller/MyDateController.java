package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.service.MyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyDateController {

    private final MyDateService myDateService;
}
