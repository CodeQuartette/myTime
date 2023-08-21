package com.codeQuartette.myTime.repository.impl;

import com.codeQuartette.myTime.domain.MyDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyDateBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllIgnore(List<MyDate> myDates){
        String insertSql = "INSERT IGNORE INTO my_date (date, user_id)" +
                "VALUES (?, ?)";

       jdbcTemplate.batchUpdate(insertSql,
                myDates,
                myDates.size(),
                (PreparedStatement ps, MyDate myDate) -> {
                    ps.setString(1, myDate.getDate().toString());
                    ps.setString(2, myDate.getUser().getId().toString());
                });

    }
}
