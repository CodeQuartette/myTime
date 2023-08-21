package com.codeQuartette.myTime.controller.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class HabitDTOTest {

    @Test
    void convertStringToArray() {
        String str = "[\"MON\", \"FRI\"]";

        String[] repeatDay = Arrays.stream(str.split(",")).map(s -> s.replaceAll("[\"\s\\[\\]]", "")).toArray(String[]::new);
        Arrays.stream(repeatDay).forEach(System.out::println);
    }
}
