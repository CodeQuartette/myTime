package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.annotation.UserId;
import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseType;
import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.service.ScheduleService;
import lombok.RequiredArgsConstructor;
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
    public ResponseDTO<ScheduleDTO.ResponseList> find(@UserId Long userId,
                                                      @RequestParam(name = "scheduleId", required = false) Long scheduleId,
                                                      @RequestParam(name = "date", required = false) LocalDate date,
                                                      @RequestParam(name = "yearMonth", required = false) YearMonth yearMonth) {

        List<Schedule> schedules = new ArrayList<>();

        if (scheduleId != null) {
            schedules = scheduleService.find(userId,scheduleId);
        } else if (date != null) {
            schedules = scheduleService.find(userId, date);
        } else if (yearMonth != null) {
            schedules = scheduleService.find(userId, yearMonth);
        }

        ScheduleDTO.ResponseList responseList = ScheduleDTO.ResponseList.builder()
                .schedules(schedules.stream()
                        .map(schedule -> ScheduleDTO.Response.builder()
                                .id(schedule.getId())
                                .title(schedule.getTitle())
                                .color(schedule.getColor())
                                .startDate(schedule.getStartDateTime())
                                .endDate(schedule.getEndDateTime())
                                .isSpecificTime(schedule.getIsSpecificTime())
                                .alert(schedule.getAlert()).build())
                        .collect(Collectors.toList()))
                .build();

        return ResponseDTO.from(ResponseType.SUCCESS, responseList);
    }

    @PostMapping
    public ResponseDTO<?> create(@UserId Long userId,
                                 @RequestBody ScheduleDTO.Request request) {
        scheduleService.create(userId, request);
        return ResponseDTO.from(ResponseType.CREATED);
    }

    @PutMapping
    public ResponseDTO<ScheduleDTO.Response> update(@UserId Long userId,
                                                    @RequestParam(name = "id") Long scheduleId,
                                                    @RequestBody ScheduleDTO.Request request) {
        Schedule schedule = scheduleService.update(userId, scheduleId, request);
        ScheduleDTO.Response response = ScheduleDTO.Response.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .color(schedule.getColor())
                .startDate(schedule.getStartDateTime())
                .endDate(schedule.getEndDateTime())
                .isSpecificTime(schedule.getIsSpecificTime())
                .alert(schedule.getAlert())
                .build();
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @DeleteMapping
    public ResponseDTO<?> delete(@UserId Long userId,
                                 @RequestParam(name = "id") Long scheduleId) {
        scheduleService.delete(userId, scheduleId);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }
}
