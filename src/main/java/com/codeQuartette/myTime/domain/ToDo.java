package com.codeQuartette.myTime.domain;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.value.Color;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2334566915945398092L;

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

    public static ToDo create(ToDoDTO.Request toDoRequestDTO, MyDate myDate) {
        return ToDo.builder()
                .title(toDoRequestDTO.getTitle())
                .color(toDoRequestDTO.getColor())
                .date(toDoRequestDTO.getDate())
                .isDone(Boolean.FALSE)
                .isBlind(toDoRequestDTO.getIsBlind())
                .myDate(myDate)
                .build();
    }

    public void update(ToDoDTO.Request toDoRequestDTO) {
        this.title = toDoRequestDTO.getTitle() == null ? this.title : toDoRequestDTO.getTitle();
        this.color = toDoRequestDTO.getColor() == null ? this.color : toDoRequestDTO.getColor();
        this.date = toDoRequestDTO.getDate() == null ? this.date : toDoRequestDTO.getDate();
        this.isBlind = toDoRequestDTO.getIsBlind() == null ? this.isBlind : toDoRequestDTO.getIsBlind();
    }

    public void updateDone(ToDoDTO.Request toDoRequestDTO) {
        this.isDone = toDoRequestDTO.getIsDone() == null ? this.isDone : toDoRequestDTO.getIsDone();
    }
}
