package com.codeQuartette.myTime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HabitHasMyDateId implements Serializable {

    @Column(name = "habitId")
    public Long habitId;

    @Column(name = "myDateId")
    public Long myDateId;
}
