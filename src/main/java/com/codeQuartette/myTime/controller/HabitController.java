package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.controller.dto.HabitHasMyDateDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseType;
import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.service.HabitHasMyDateService;
import com.codeQuartette.myTime.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;
    private final HabitHasMyDateService habitHasMyDateService;

    @PostMapping("/habit")
    public ResponseDTO<String> create(@RequestParam Long userId, @RequestBody HabitDTO.Request habitRequestDTO) {
        habitService.create(userId, habitRequestDTO);
        return ResponseDTO.from(ResponseType.CREATED);
    }

    @PatchMapping("/habit")
    public ResponseDTO<String> update(@RequestParam Long userId, @RequestParam Long id, @RequestBody HabitDTO.Request habitRequestDTO) {
        habitService.update(userId, id, habitRequestDTO);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }

    @DeleteMapping("/habit")
    public ResponseDTO<String> delete(@RequestParam Long id) {
        habitService.delete(id);
        return ResponseDTO.from(ResponseType.SUCCESS);
    }

    @GetMapping(value = "/habit", params = "id")
    public ResponseDTO<HabitDTO.Response> getHabitById(@RequestParam(name = "id") Long id) {
        Habit habit = habitService.findHabit(id);
        return ResponseDTO.from(ResponseType.SUCCESS, HabitDTO.Response.of(habit));
    }

    @GetMapping(value = "/habit", params = {"userId", "date"})
    public ResponseDTO<List<HabitHasMyDateDTO.Response>> getHabitByDate(@RequestParam(name = "userId") Long userId, @RequestParam(name = "date") LocalDate date) {
        List<HabitHasMyDate> habitHasMyDates = habitHasMyDateService.findAllHabitHasMyDate(userId, date);
        List<HabitHasMyDateDTO.Response> response = habitHasMyDates.stream().map(habitHasMyDate -> HabitHasMyDateDTO.Response.of(habitHasMyDate)).toList();
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @GetMapping(value = "/habit", params = {"userId", "yearMonth"})
    public ResponseDTO<List<HabitHasMyDateDTO.Response>> getHabitByMonth(@RequestParam(name = "userId") Long userId, @RequestParam YearMonth yearMonth) {
        List<HabitHasMyDate> habitHasMyDates = habitHasMyDateService.findAllHabitHasMyDate(userId, yearMonth);
        List<HabitHasMyDateDTO.Response> response = habitHasMyDates.stream().map(habitHasMyDate -> HabitHasMyDateDTO.Response.of(habitHasMyDate)).toList();
        return ResponseDTO.from(ResponseType.SUCCESS, response);
    }

    @GetMapping("/category")
    public ResponseDTO<List<String>> getCategory() {
        return ResponseDTO.from(ResponseType.SUCCESS, habitService.getCategory());
    }

}
