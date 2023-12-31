package com.codeQuartette.myTime.repository.impl;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HabitHasMyDateBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<HabitHasMyDate> habitHasMyDates) {
        String sql = "INSERT IGNORE INTO habit_has_my_date (habit_id, my_date_id)" +
                "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql,
                habitHasMyDates,
                habitHasMyDates.size(),
                (PreparedStatement ps, HabitHasMyDate habitHasMyDate) -> {
                    ps.setString(1, habitHasMyDate.getHabit().getId().toString());
                    ps.setString(2, habitHasMyDate.getMyDate().getId().toString());
                });
    }

    @Transactional
    public void deleteAllNotDone(Long habitId) {
        String sql = "DELETE FROM habit_has_my_date where habit_id = " + habitId +
                " AND is_done = false";

        jdbcTemplate.update(sql);
    }
}
