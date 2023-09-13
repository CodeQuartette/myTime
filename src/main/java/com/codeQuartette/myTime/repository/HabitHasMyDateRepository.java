package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.HabitHasMyDateId;
import com.codeQuartette.myTime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitHasMyDateRepository extends JpaRepository<HabitHasMyDate, HabitHasMyDateId> {

    List<HabitHasMyDate> findAllByMyDate_DateIs(LocalDate date);

    List<HabitHasMyDate> findAllByMyDate_UserAndMyDate_DateIs(User user, LocalDate date);

    List<HabitHasMyDate> findAllByMyDate_DateBetween(LocalDate startDate, LocalDate endDate);

    List<HabitHasMyDate> findAllByMyDate_UserAndMyDate_DateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<HabitHasMyDate> findAllByHabitAndIsDoneIsFalse(Habit habit);
}
