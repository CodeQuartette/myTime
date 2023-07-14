package com.codeQuartette.myTime.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ToDoInfoRequestDTO {

    private String title;

    private String color;

    private LocalDate day;

    private Boolean isBlind;
}
