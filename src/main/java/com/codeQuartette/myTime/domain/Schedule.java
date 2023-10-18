package com.codeQuartette.myTime.domain;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.value.Color;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule implements Serializable {

    @Serial
    private static final long serialVersionUID = 5070641913231617118L;

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

    public void update(ScheduleDTO.Request request) {
        this.title = request.getTitle() == null ? this.getTitle() : request.getTitle();
        this.color = request.getColor() == null ? this.color : request.getColor();
        this.startDateTime = request.getStartDate() == null ? this.startDateTime : request.getStartDate();
        this.endDateTime = request.getEndDate() == null ? this.endDateTime : request.getEndDate();
        this.alert = request.getAlert() == null ? this.getAlert() : request.getAlert();
        this.isSpecificTime = request.getIsSpecificTime() == null ? this.getIsSpecificTime() : request.getIsSpecificTime();
    }
}
