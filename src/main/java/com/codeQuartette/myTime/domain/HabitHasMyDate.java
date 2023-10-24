package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitHasMyDate implements Serializable {

    @Serial
    private static final long serialVersionUID = 9218743661454962268L;

    @EmbeddedId
    private HabitHasMyDateId habitHasMyDateId;

    @ManyToOne
    @JoinColumn(name = "habitId")
    @MapsId("habitId")
    private Habit habit;

    @ManyToOne
    @JoinColumn(name = "myDateId")
    @MapsId("myDateId")
    private MyDate myDate;

    @Builder.Default
    @ColumnDefault("false")
    private Boolean isDone = false;
}
