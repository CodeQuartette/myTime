package com.codeQuartette.myTime.domain;


import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.domain.value.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private String repeatDay;
    private String categoryContent;
    private Boolean isBlind;

    @Enumerated(EnumType.STRING)
    private Category category;

    public static Habit create(HabitDTO.Request habitRequestDTO) {
        return Habit.builder()
                .startDate(habitRequestDTO.getStartDate())
                .repeatDay(Arrays.toString(habitRequestDTO.getRepeatDay()))
                .category(habitRequestDTO.getCategory())
                .categoryContent(habitRequestDTO.getCategoryContent())
                .isBlind(habitRequestDTO.isBlind())
                .build();
    }
}
