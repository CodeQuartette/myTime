package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    @Query(value = "select h.id, h.start_date, h.end_date, h.repeat_day, h.category_content, h.is_blind, h.category from habit h\n" +
            "join my_date m " +
            "join habit_has_my_date hd\n" +
            "where m.id = hd.my_date_id\n" +
            "and h.id = hd.habit_id\n" +
            "and m.date = :date",
        nativeQuery = true)
    List<Habit> findAllHabitByDate(LocalDate date);
}
