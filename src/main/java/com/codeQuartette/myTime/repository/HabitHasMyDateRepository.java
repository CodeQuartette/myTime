package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.HabitHasMyDateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitHasMyDateRepository extends JpaRepository<HabitHasMyDate, HabitHasMyDateId> {

    List<HabitHasMyDate> findAllByMyDate_UserAndMyDate_DateIs(User user, LocalDate date);

    List<HabitHasMyDate> findAllByMyDate_UserAndMyDate_DateBetween(User user, LocalDate startDate, LocalDate endDate);

}
