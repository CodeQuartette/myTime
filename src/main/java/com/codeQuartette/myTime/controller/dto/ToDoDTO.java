package com.codeQuartette.myTime.controller.dto;

import com.codeQuartette.myTime.domain.value.Color;
import lombok.Getter;
import java.time.LocalDate;

public class ToDoDTO {

    @Getter
     public static class Request{
        private String title;

        private Color color;

        private LocalDate date;

        private Boolean isBlind;


    }

    public static class Response{


    }
}
