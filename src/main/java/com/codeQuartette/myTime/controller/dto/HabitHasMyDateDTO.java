package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.value.Category;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public class HabitHasMyDateDTO {

    @Builder
    @Getter
    public static class Response {

        private Long habitId;

        private LocalDate startDate;

        @Nullable
        private LocalDate endDate;

        private Boolean isBlind;

        private Category category;

        private String categoryContent;

        private LocalDate date;

        private boolean isDone;

        public static HabitHasMyDateDTO.Response of(HabitHasMyDate habitHasMyDate) {
            return Response.builder()
                    .habitId(habitHasMyDate.getHabit().getId())
                    .startDate(habitHasMyDate.getHabit().getStartDate())
                    .endDate(habitHasMyDate.getHabit().getEndDate())
                    .isBlind(habitHasMyDate.getHabit().getIsBlind())
                    .category(habitHasMyDate.getHabit().getCategory())
                    .categoryContent(habitHasMyDate.getHabit().getCategoryContent())
                    .date(habitHasMyDate.getMyDate().getDate())
                    .isDone(habitHasMyDate.getIsDone())
                    .build();
        }

    }
}
