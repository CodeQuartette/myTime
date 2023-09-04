package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.domain.value.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ColorController {

    @GetMapping("/color")
    public ResponseEntity<List<String>> getColors() {
        return ResponseEntity.ok(Color.getColors());
    }
}
