package com.codeQuartette.myTime.repository.impl;

import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.repository.ScheduleSelectRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static com.codeQuartette.myTime.domain.QMyDate.myDate;
import static com.codeQuartette.myTime.domain.QSchedule.schedule;
import static com.codeQuartette.myTime.domain.QScheduleHasMyDate.scheduleHasMyDate;

@RequiredArgsConstructor
public class ScheduleSelectRepositoryImpl implements ScheduleSelectRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Schedule> findByDate(Long userId, LocalDate date) {
        return jpaQueryFactory.select(schedule)
                .from(myDate)
                .join(scheduleHasMyDate).on(myDate.id.eq(scheduleHasMyDate.myDate.id))
                .join(schedule).on(scheduleHasMyDate.schedule.id.eq(schedule.id))
                .where(myDate.user.id.eq(userId).and(myDate.date.eq(date)))
                .fetch();
    }

    public List<Schedule> findByYearMonth(Long userId, YearMonth yearMonth) {
        return jpaQueryFactory.select(schedule)
                .from(myDate)
                .join(scheduleHasMyDate).on(myDate.id.eq(scheduleHasMyDate.myDate.id))
                .join(schedule).on(scheduleHasMyDate.schedule.id.eq(schedule.id))
                .where(myDate.user.id.eq(userId)
                        .and(myDate.date.year().eq(yearMonth.getYear()))
                        .and(myDate.date.month().eq(yearMonth.getMonth().getValue())))
                .fetch();
    }
}
