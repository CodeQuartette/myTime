package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleHasMyDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "my_date_id")
    private MyDate myDate;

    public ScheduleHasMyDate update(MyDate myDate) {
        this.myDate = myDate;
        return this;
    }
}
