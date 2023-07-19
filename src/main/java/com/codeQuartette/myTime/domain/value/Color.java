package com.codeQuartette.myTime.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

import java.util.Arrays;

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
                .orElseThrow(() -> new RuntimeException("해당 컬러는 없습니다"));
    }
}
