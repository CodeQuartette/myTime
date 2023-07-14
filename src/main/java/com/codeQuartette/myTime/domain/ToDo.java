package com.codeQuartette.myTime.domain;

import com.codeQuartette.myTime.controller.dto.request.ToDoInfoRequestDTO;
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

    @ManyToOne
    @JoinColumn(name = "my_date_id")
    private MyDate myDate;

    public static ToDo create(ToDoInfoRequestDTO toDoInfoRequestDTO) {
        return ToDo.builder()
                .title(toDoInfoRequestDTO.getTitle())
                .color(Color.convertor(toDoInfoRequestDTO.getColor()))
                .date(toDoInfoRequestDTO.getDay())
                .isDone(Boolean.FALSE)
                .isBlind(toDoInfoRequestDTO.getIsBlind())
                .build();
    }
}
