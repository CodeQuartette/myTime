package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.value.Color;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ToDoDTO {

    @Builder
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
     public static class Request{

        private String title;

        private Color color;

        private LocalDate date;

        private Boolean isDone;

        private Boolean isBlind;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseList{

        private List<Response> toDos;

        public static ToDoDTO.ResponseList of(List<Response> toDoList) {
            return ResponseList.builder()
                    .toDos(toDoList)
                    .build();
        }
    }
}
