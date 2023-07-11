package com.codeQuartette.myTime.domain;


import com.codeQuartette.myTime.domain.value.Category;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDay;
    private String repeatDay;
    private String categoryContent;
    private Boolean isBlind;

    @Enumerated(EnumType.STRING)
    private Category category;
}
