package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.domain.value.Category;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.Arrays;

public class HabitDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request {

        private LocalDate startDate;

        @Nullable
        private LocalDate endDate;

        private String[] repeatDay;

        private Category category;

        private String categoryContent;

        private Boolean isBlind;
    }

    @Builder
    @Getter
    public static class Response {

        private Long id;

        private LocalDate startDate;

        private LocalDate endDate;

        private String[] repeatDay;

        private Category category;

        private String categoryContent;

        private boolean isBlind;

        public static HabitDTO.Response of(Habit habit) {
            String[] repeatDay = Arrays.stream(habit.getRepeatDay().split(",")).map(s -> s.replaceAll("[\"\s\\[\\]]", "")).toArray(String[]::new);

            return Response.builder()
                    .id(habit.getId())
                    .startDate(habit.getStartDate())
                    .endDate(habit.getEndDate())
                    .repeatDay(repeatDay)
                    .category(habit.getCategory())
                    .categoryContent(habit.getCategoryContent())
                    .isBlind(habit.getIsBlind())
                    .build();
        }
    }
}
