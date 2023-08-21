package com.codeQuartette.myTime.domain;


import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.domain.value.Category;
import com.codeQuartette.myTime.exception.CannotCreateHabitException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String repeatDay;
    private String categoryContent;
    private Boolean isBlind;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitHasMyDate> habitHasMyDates = new ArrayList<>();

    public static LocalDate checkStartDate(LocalDate startDate) {
        if(startDate.isBefore(LocalDate.now())) {
            throw new CannotCreateHabitException("시작 날짜가 오늘 날짜보다 이전일 수 없습니다.");
        }
        return startDate;
    }

    public static LocalDate checkEndDate(LocalDate startDate, LocalDate endDate) {
        if(endDate == null) {
            return startDate.plusYears(3L);
        } else {
            if(startDate.plusYears(3L).isBefore(endDate)) {
                throw new CannotCreateHabitException("습관은 3년 이상으로 생성할 수 없습니다. 3년 뒤 갱신해주세요.");
            }
        }
        return endDate;
    }

    public static Habit create(HabitDTO.Request habitRequestDTO) {

        return Habit.builder()
                .startDate(checkStartDate(habitRequestDTO.getStartDate()))
                .endDate(checkEndDate(habitRequestDTO.getStartDate(), habitRequestDTO.getEndDate()))
                .repeatDay(Arrays.toString(habitRequestDTO.getRepeatDay()))
                .category(habitRequestDTO.getCategory())
                .categoryContent(habitRequestDTO.getCategoryContent())
                .isBlind(habitRequestDTO.getIsBlind())
                .build();
    }

    public void update(HabitDTO.Request habitRequestDTO) {
        this.startDate = habitRequestDTO.getStartDate() == null ? this.startDate : habitRequestDTO.getStartDate();
        this.repeatDay = Arrays.toString(habitRequestDTO.getRepeatDay()) == null ? this.repeatDay : Arrays.toString(habitRequestDTO.getRepeatDay());
        this.category = habitRequestDTO.getCategory() == null ? this.category : habitRequestDTO.getCategory();
        this.categoryContent = habitRequestDTO.getCategoryContent() == null ? this.categoryContent : habitRequestDTO.getCategoryContent();
        this.isBlind = habitRequestDTO.getIsBlind() == null ? this.isBlind : habitRequestDTO.getIsBlind();
    }

    public void updateAllHabitHasMyDates(List<HabitHasMyDate> habitHasMyDates) {
        this.habitHasMyDates.addAll(habitHasMyDates);
    }
}
