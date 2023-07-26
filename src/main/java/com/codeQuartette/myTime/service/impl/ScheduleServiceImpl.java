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

import java.util.List;
import java.util.stream.Collectors;

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


    @Override
    @Transactional
    public Schedule create(Long userId, ScheduleDTO.create request) {
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
}
