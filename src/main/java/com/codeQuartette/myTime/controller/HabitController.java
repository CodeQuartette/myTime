package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.controller.dto.HabitHasMyDateDTO;
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
    public void create(@RequestParam Long userId, @RequestBody HabitDTO.Request habitRequestDTO) {
        habitService.create(userId, habitRequestDTO);
    }

    @PatchMapping("/habit")
    public void update(@RequestParam Long userId, @RequestParam Long id, @RequestBody HabitDTO.Request habitRequestDTO) {
        habitService.update(userId, id, habitRequestDTO);
    }

    @DeleteMapping("/habit")
    public void delete(@RequestParam Long id) {
        habitService.delete(id);
    }

    @GetMapping(value = "/habit", params = "id")
    public HabitDTO.Response getHabitById(@RequestParam(name = "id") Long id) {
        Habit habit = habitService.findHabit(id);
        return HabitDTO.Response.of(habit);
    }

    @GetMapping(value = "/habit", params = {"userId", "date"})
    public List<HabitHasMyDateDTO.Response> getHabitByDate(@RequestParam(name = "userId") Long userId, @RequestParam(name = "date") LocalDate date) {
        List<HabitHasMyDate> habitHasMyDates = habitHasMyDateService.findAllHabitHasMyDate(userId, date);
        return habitHasMyDates.stream().map(habitHasMyDate -> HabitHasMyDateDTO.Response.of(habitHasMyDate)).toList();
    }

    @GetMapping(value = "/habit", params = {"userId", "yearMonth"})
    public List<HabitHasMyDateDTO.Response> getHabitByMonth(@RequestParam(name = "userId") Long userId, @RequestParam YearMonth yearMonth) {
        List<HabitHasMyDate> habitHasMyDates = habitHasMyDateService.findAllHabitHasMyDate(userId, yearMonth);
        return habitHasMyDates.stream().map(habitHasMyDate -> HabitHasMyDateDTO.Response.of(habitHasMyDate)).toList();
    }

    @GetMapping("/category")
    public ResponseEntity<List<String>> getCategory() {
        return ResponseEntity.ok(habitService.getCategory());
    }

}
