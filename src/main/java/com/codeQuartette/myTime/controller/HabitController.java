package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.controller.dto.HabitHasMyDateDTO;
import com.codeQuartette.myTime.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping("/habit")
    public void create(@RequestParam Long userId, @RequestBody HabitDTO.Request habitRequestDTO) {
        habitService.create(userId, habitRequestDTO);
    }

    @PatchMapping("/habit")
    public void update(@RequestParam Long id, @RequestBody HabitDTO.Request habitRequestDTO) {
        habitService.update(id, habitRequestDTO);
    }

    @DeleteMapping("/habit")
    public void delete(@RequestParam Long id) {
        habitService.delete(id);
    }

    @GetMapping(value = "/habit", params = "id")
    public HabitDTO.Response getHabitById(@RequestParam(name = "id") Long id) {
        return habitService.getHabitById(id);
    }

    @GetMapping("/habits")
    public List<HabitDTO.Response> getHabitByDate(@RequestParam LocalDate date) {
        return habitService.getHabitByDate(date);
    }

    @GetMapping(value = "/habit", params = "month")
    public List<HabitHasMyDateDTO.Response> getHabitByMonth(@RequestParam YearMonth yearMonth) {
        return habitService.getHabitByMonth(yearMonth);
    }

    @GetMapping("/category")
    public List<String> getCategory() {
        return habitService.getCategory();
    }

}
