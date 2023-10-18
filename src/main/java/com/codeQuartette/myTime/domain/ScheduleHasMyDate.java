package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleHasMyDate implements Serializable {

    @Serial
    private static final long serialVersionUID = 2180310186780008619L;

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
