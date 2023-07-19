package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.HabitHasDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HabitHasDateBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<HabitHasDate> habitHasDates) {
        String sql = "INSERT INTO habit_has_date (habit_id, my_date_id)" +
                "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql,
                habitHasDates,
                habitHasDates.size(),
                (PreparedStatement ps, HabitHasDate habitHasDate) -> {
                    ps.setString(1, habitHasDate.getHabit().getId().toString());
                    ps.setString(2, habitHasDate.getMyDate().getId().toString());
                });
    }
}
