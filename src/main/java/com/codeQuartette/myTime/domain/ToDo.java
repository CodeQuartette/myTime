package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String title;
    private Boolean isDone;
    private Boolean isBlind;

    @ManyToOne
    @JoinColumn(name = "my_date_id")
    private MyDate myDate;
}
