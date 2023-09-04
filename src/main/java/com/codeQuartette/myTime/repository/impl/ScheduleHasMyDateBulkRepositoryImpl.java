package com.codeQuartette.myTime.repository.impl;

import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import com.codeQuartette.myTime.repository.ScheduleHasMyDateBulkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
public class ScheduleHasMyDateBulkRepositoryImpl implements ScheduleHasMyDateBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<ScheduleHasMyDate> scheduleHasMyDates) {
        String sql = "INSERT INTO schedule_has_my_date (schedule_id, my_date_id)" +
                "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql,
                scheduleHasMyDates,
                scheduleHasMyDates.size(),
                (PreparedStatement ps, ScheduleHasMyDate scheduleHasMyDate) -> {
                    ps.setString(1, scheduleHasMyDate.getSchedule().getId().toString());
                    ps.setString(2, scheduleHasMyDate.getMyDate().getId().toString());
                });
    }

    @Transactional
    public void deleteAll(List<ScheduleHasMyDate> scheduleHasMyDates) {
        String sql = "DELETE FROM schedule_has_my_date WHERE schedule_id = ?";

        jdbcTemplate.batchUpdate(sql,
                scheduleHasMyDates,
                scheduleHasMyDates.size(),
                (PreparedStatement ps, ScheduleHasMyDate scheduleHasMyDate) -> {
                    ps.setString(1, scheduleHasMyDate.getSchedule().getId().toString());
                });
    }
}
