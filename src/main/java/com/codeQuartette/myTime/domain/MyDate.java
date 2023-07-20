package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    @JoinColumn(name = "my_date_id", nullable = false)
    private List<ToDo> toDos = new ArrayList<>();

    public void addToDo(ToDo toDo) {

        this.toDos.add(toDo);
    }
}
