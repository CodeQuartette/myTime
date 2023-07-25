package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "myDate", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HabitHasMyDate> habitHasMyDates = new ArrayList<>();

    public MyDate(LocalDate date, User user) {
        this.date = date;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDate myDate = (MyDate) o;
        return date.equals(myDate.date) && user.equals(myDate.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, user);
    }

    @Override
    public String toString() {
        return "MyDate{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + user.getNickname() +
                '}';
    }
}
