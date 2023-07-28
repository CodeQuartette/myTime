package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.HabitHasMyDateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitHasMyDateRepository extends JpaRepository<HabitHasMyDate, HabitHasMyDateId> {
}
