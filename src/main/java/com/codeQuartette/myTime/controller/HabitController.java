package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
