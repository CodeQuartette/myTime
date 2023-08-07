package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /*
     * TODO: AOP로 USER PARAMETER 넘기기
     * 일단 REQUEST PARAM으로 USER 정보 받기
     * */

    @GetMapping
    public ResponseEntity<ScheduleDTO.ResponseList> find(@RequestParam(name = "scheduleId", required = false) Long scheduleId,
                                                         @RequestParam(name = "userId", required = false) Long userId,
                                                         @RequestParam(name = "date", required = false) LocalDate date,
                                                         @RequestParam(name = "yearMonth", required = false) YearMonth yearMonth) {
        List<Schedule> schedules = new ArrayList<>();

        if (scheduleId != null) {
            schedules = scheduleService.find(scheduleId);
        } else if (userId != null && date != null) {
            schedules = scheduleService.find(userId, date);
        } else if (userId != null && yearMonth != null) {
            schedules = scheduleService.find(userId, yearMonth);
        }

        return ResponseEntity.ok(ScheduleDTO.ResponseList.builder()
                .schedules(schedules.stream()
                        .map(schedule -> ScheduleDTO.Response.builder()
                                .id(schedule.getId())
                                .title(schedule.getTitle())
                                .color(schedule.getColor())
                                .startDate(schedule.getStartDateTime())
                                .endDate(schedule.getEndDateTime())
                                .isSpecificTime(schedule.getIsSpecificTime())
                                .alert(schedule.getAlert())
                                .build())
                        .collect(Collectors.toList()))
                .build());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestParam(name = "userId") Long userId,
                                         @RequestBody ScheduleDTO.Request request) {
        scheduleService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam(name = "userId") Long userId,
                                         @RequestParam(name = "id") Long scheduleId) {
        scheduleService.delete(userId, scheduleId);
        return ResponseEntity.ok("OK");
    }

}
