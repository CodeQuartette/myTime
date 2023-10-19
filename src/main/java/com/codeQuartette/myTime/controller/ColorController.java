package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.globalResponse.ResponseDTO;
import com.codeQuartette.myTime.controller.globalResponse.ResponseType;
import com.codeQuartette.myTime.domain.value.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ColorController {

    @GetMapping("/color")
    public ResponseDTO<List<String>> getColors() {
        return ResponseDTO.from(ResponseType.SUCCESS,Color.getColors());
    }
}
