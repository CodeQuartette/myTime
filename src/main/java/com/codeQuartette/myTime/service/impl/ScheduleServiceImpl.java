package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.exception.ScheduleNotFoundException;
import com.codeQuartette.myTime.repository.ScheduleRepository;
import com.codeQuartette.myTime.service.MyDateService;
import com.codeQuartette.myTime.service.ScheduleHasMyDateService;
import com.codeQuartette.myTime.service.ScheduleService;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserService userService;
    private final MyDateService myDateService;
    private final ScheduleHasMyDateService scheduleHasMyDateService;

    private Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFoundException());
    }

    private Schedule saveSchedule(ScheduleDTO.Request requestDTO) {
        return scheduleRepository.save(Schedule.builder()
                .title(requestDTO.getTitle())
                .color(requestDTO.getColor())
                .startDateTime(requestDTO.getStartDate())
                .endDateTime(requestDTO.getEndDate())
                .isSpecificTime(requestDTO.getIsSpecificTime())
                .alert(requestDTO.getAlert())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> find(Long scheduleId) {
        return List.of(findSchedule(scheduleId));
    }

    @Override
    @Cacheable(cacheNames = "find-schedule", key = "#userId.toString() + ':' + #date")
    public List<Schedule> find(Long userId, LocalDate date) {
        return scheduleRepository.findByDate(userId, date);
    }

    @Override
    @Cacheable(cacheNames = "find-schedule", key = "#userId.toString() + ':' + #yearMonth")
    public List<Schedule> find(Long userId, YearMonth yearMonth) {
        return scheduleRepository.findByYearMonth(userId, yearMonth);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "find-schedule", allEntries = true)
    public Schedule create(Long userId, ScheduleDTO.Request request) {
        // 1. user 찾기
        User user = userService.findUser(userId);

        // 2. myDateList 만들기
        List<MyDate> myDates = myDateService.saveAllMyDate(
                request.getStartDate().toLocalDate()
                        .datesUntil(request.getEndDate().toLocalDate().plusDays(1))
                        .map(date -> MyDate.builder()
                                .user(user)
                                .date(date)
                                .build())
                        .collect(Collectors.toList()));

        // 3. Schedule 만들기
        Schedule schedule = saveSchedule(request);

        // 4. ScheduleHasMyDate List 만들기
        List<ScheduleHasMyDate> scheduleHasMyDates = myDates.stream()
                .map(myDate -> ScheduleHasMyDate.builder()
                        .schedule(schedule)
                        .myDate(myDate)
                        .build())
                .collect(Collectors.toList());

        scheduleHasMyDateService.saveAll(scheduleHasMyDates);

        return schedule;
    }

    @Override
    @CacheEvict(cacheNames = "find-schedule", allEntries = true)
    public void delete(Long userId, Long scheduleId) {
        User user = userService.findUser(userId);

        Schedule schedule = findSchedule(scheduleId);
        scheduleRepository.delete(schedule);
    }

    @Override
    @CacheEvict(cacheNames = "find-schedule", allEntries = true)
    public Schedule update(Long userId, Long scheduleId, ScheduleDTO.Request request) {
        User user = userService.findUser(userId);

        Schedule schedule = findSchedule(scheduleId);

        if (request.getStartDate() != null || request.getEndDate() != null) {

            List<MyDate> myDates = myDateService.saveAllMyDate(
                    request.getStartDate().toLocalDate()
                            .datesUntil(request.getEndDate().toLocalDate().plusDays(1))
                            .map(date -> MyDate.builder()
                                    .user(user)
                                    .date(date)
                                    .build())
                            .collect(Collectors.toList()));


            List<ScheduleHasMyDate> oldScheduleHasMyDates = scheduleHasMyDateService.findScheduleHasMyDate(schedule);
            scheduleHasMyDateService.deleteAll(oldScheduleHasMyDates);


            // 4. ScheduleHasMyDate List 만들기
            List<ScheduleHasMyDate> newScheduleHasMyDates = myDates.stream()
                    .map(myDate -> ScheduleHasMyDate.builder()
                            .schedule(schedule)
                            .myDate(myDate)
                            .build())
                    .collect(Collectors.toList());

            scheduleHasMyDateService.saveAll(newScheduleHasMyDates);
        }
        schedule.update(request);

        return scheduleRepository.save(schedule);
    }
}
