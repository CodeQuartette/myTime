package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.domain.value.Category;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.Arrays;

public class HabitDTO {

    @Getter
    public static class Request {

        private LocalDate startDate;

        @Nullable
        private LocalDate endDate;

        private String[] repeatDay;

        private Category category;

        private String categoryContent;

        private boolean isBlind;
    }

    @Builder
    @Getter
    public static class Response {

        private LocalDate startDate;

        private String[] repeatDay;

        private Category category;

        private String categoryContent;

        private boolean isBlind;

        public static HabitDTO.Response of(Habit habit) {
            String[] repeatDay = Arrays.stream(habit.getRepeatDay().split(",")).map(s -> s.replaceAll("[\"\s\\[\\]]", "")).toArray(String[]::new);

            return Response.builder()
                    .startDate(habit.getStartDate())
                    .repeatDay(repeatDay)
                    .category(habit.getCategory())
                    .categoryContent(habit.getCategoryContent())
                    .isBlind(habit.getIsBlind())
                    .build();
        }
    }
}
