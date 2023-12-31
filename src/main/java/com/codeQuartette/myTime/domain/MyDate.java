package com.codeQuartette.myTime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_date_user_id", columnNames = {"date", "user_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class MyDate implements Serializable {

    @Serial
    private static final long serialVersionUID = -7185577371366593650L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "myDate", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<ToDo> toDos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "myDate", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HabitHasMyDate> habitHasMyDates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "myDate", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleHasMyDate> scheduleHasMyDates = new ArrayList<>();

    public MyDate(LocalDate date, User user) {
        this.date = date;
        this.user = user;
    }

    public boolean matchUser(Long userId) {
         return this.user.matchId(userId);
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
