package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.value.Category;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

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

    public class Response {

        private LocalDate startDate;

        private String[] repeatDay;

        private Category category;

        private String categoryContent;

        private boolean isBlind;
    }
}
