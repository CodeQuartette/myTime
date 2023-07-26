package com.codeQuartette.myTime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HabitHasMyDateId implements Serializable {

    @Column(name = "habitId")
    public Long habitId;

    @Column(name = "myDateId")
    public Long myDateId;
}
