package com.codeQuartette.myTime.domain;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.value.Color;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private Color color;

    private Boolean isDone;

    private Boolean isBlind;


    public static ToDo create(ToDoDTO.Request toDoRequestDTO) {
        return com.codeQuartette.myTime.domain.ToDo.builder()
                .title(toDoRequestDTO.getTitle())
                .color(toDoRequestDTO.getColor())
                .date(toDoRequestDTO.getDate())
                .isDone(Boolean.FALSE)
                .isBlind(toDoRequestDTO.getIsBlind())
                .build();
    }
}
