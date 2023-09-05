package com.codeQuartette.myTime.domain.value;

import com.codeQuartette.myTime.exception.ColorNotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum Color {

    FFADAD, // 빨강
    FFD6A5, // 주황
    FDFFB6, // 노랑
    CAFFBF, // 초록
    C0FDFF, // 파랑
    ADC4FF, // 남색
    BDB2FF, // 보라
    FFC6FF; // 핑크

    @JsonCreator
    public static Color convertor(String color) {
        return Arrays.stream(Color.values())
                .filter(e -> e.name().equals(color))
                .findAny()
                .orElseThrow(() -> new ColorNotFoundException());
    }

    public static List<String> getColors() {
        return Arrays.stream(Color.values()).map(color -> color.toString()).toList();
    }
}
