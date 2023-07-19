package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.ScheduleRepository;
import com.codeQuartette.myTime.service.MyDateService;
import com.codeQuartette.myTime.service.ScheduleHasMyDateService;
import com.codeQuartette.myTime.service.ScheduleService;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserService userService;
    private final MyDateService myDateService;
    private final ScheduleHasMyDateService scheduleHasMyDateService;


    private Schedule saveSchedule(ScheduleDTO.create createDTO) {
        return scheduleRepository.save(Schedule.builder()
                .title(createDTO.getTitle())
                .color(createDTO.getColor())
                .startDateTime(createDTO.getStartDate())
                .endDateTime(createDTO.getEndDate())
                .isSpecificTime(createDTO.getIsSpecificTime())
                .alert(createDTO.getAlert())
                .build());
    }

    private ScheduleHasMyDate saveScheduleHasMyDate(Schedule schedule, MyDate myDate) {
        return scheduleHasMyDateService.save(ScheduleHasMyDate.builder()
                .myDate(myDate)
                .schedule(schedule)
                .build());
    }

    private MyDate saveMyDate(User user, LocalDate date) {
        return myDateService.save(MyDate.builder()
                .date(date)
                .user(user)
                .build());
    }


    @Override
    @Transactional
    public Schedule create(Long userId, ScheduleDTO.create request) {
        // 1. user 찾기
        User user = userService.findUser(userId);

        // 2. schedule 저장
        Schedule schedule = saveSchedule(request);

        // 3. startDate -> endDate 까지 MyDate 정보 만들기 및 schedule has date 같이 만들기
        LocalDate startDate = schedule.getStartDateTime().toLocalDate();
        LocalDate endDate = schedule.getEndDateTime().plusDays(1).toLocalDate();

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

            // MyDate 에 해당하는 날짜가 있는지 확인.
            if (myDateService.existMyDate(user, date)) {
                // myDate 날짜가 있으면
                MyDate myDate = myDateService.find(user, date);
                saveScheduleHasMyDate(schedule, myDate);

            } else {
                // myDate 날짜가 없으면 myDate 생성
                MyDate myDate = saveMyDate(user, date);
                saveScheduleHasMyDate(schedule, myDate);
            }
        }

        return schedule;
    }
}
