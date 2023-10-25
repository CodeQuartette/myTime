package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.value.Color;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleDTO {

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String title;
        private Color color;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Boolean isSpecificTime;
        private Boolean alert;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private Color color;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Boolean isSpecificTime;
        private Boolean alert;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseList {
        private List<Response> schedules;
    }
}
