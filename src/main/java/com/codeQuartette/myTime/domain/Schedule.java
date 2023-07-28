package com.codeQuartette.myTime.domain;

import com.codeQuartette.myTime.domain.value.Color;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String title;

    //유저에게 알림을 보내줄지 말지를 결정하는 여부
    private Boolean alert;

    //startDateTime에 시간이 붙었는지 아닌지를 확인하기 위해서
    private Boolean isSpecificTime;

    @Column
    @Enumerated(EnumType.STRING)
    private Color color;

    @Builder.Default
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleHasMyDate> scheduleHasMyDates = new ArrayList<>();

}
