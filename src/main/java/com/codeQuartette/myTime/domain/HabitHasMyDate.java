package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitHasMyDate {

    @EmbeddedId
    private HabitHasMyDateId habitHasMyDateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitId")
    @MapsId("habitId")
    private Habit habit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myDateId")
    @MapsId("myDateId")
    private MyDate myDate;

    @ColumnDefault("false")
    private Boolean isDone = false;
}
