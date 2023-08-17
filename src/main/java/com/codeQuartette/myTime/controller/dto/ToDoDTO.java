package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.value.Color;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

public class ToDoDTO {

    @Getter
     public static class Request{

        private String title;

        private Color color;

        private LocalDate date;

        private Boolean isDone;

        private Boolean isBlind;
    }

    @Builder
    @Getter
    public static class Response{

        private Long id;

        private String title;

        private Color color;

        private LocalDate date;

        private Boolean isDone;

        private Boolean isBlind;

        public static ToDoDTO.Response of(ToDo toDo) {
            return Response.builder()
                    .id(toDo.getId())
                    .title(toDo.getTitle())
                    .color(toDo.getColor())
                    .date(toDo.getDate())
                    .isDone(toDo.getIsDone())
                    .isBlind(toDo.getIsBlind())
                    .build();
        }
    }
}
