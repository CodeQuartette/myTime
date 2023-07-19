package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /*
     * TODO: AOP로 USER PARAMETER 넘기기
     * 일단 REQUEST PARAM으로 USER 정보 받기
     * */

    @PostMapping
    public ResponseEntity<String> create(@RequestParam(name = "userId") Long userId,
                                         @RequestBody ScheduleDTO.create request) {
        scheduleService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

}
