package com.codeQuartette.myTime.domain.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum Category {

    EXERCISE("운동"),
    TAKE_MEDICINE("약 먹기"),
    WEIGHING("몸무게 재기"),
    MEMORIZE_ENGLISH_WORDS("영어단어 외우기"),
    DRINK_WATER("물마시기"),
    JOGGING("야외걷기"),
    WRITE_A_DIARY("다이어리 쓰기"),
    CLEAN_HOUSE("집 청소하기");

    String description;

    @JsonCreator
    public static Category convertor(String category) {
        return Arrays.stream(Category.values())
                .filter(e -> e.name().equals(category))
                .findAny()
                .orElseThrow(() -> new RuntimeException("해당 카테고리는 없습니다"));
    }
}
