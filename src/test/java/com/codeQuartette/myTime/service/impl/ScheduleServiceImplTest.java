package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.domain.value.Color;
import com.codeQuartette.myTime.repository.MyDateRepository;
import com.codeQuartette.myTime.repository.ScheduleRepository;
import com.codeQuartette.myTime.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ScheduleServiceImplTest {

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MyDateRepository myDateRepository;

    @Autowired
    private UserRepository userRepository;

    User findUser(Long userId){
        return userRepository.findById(userId).get();
    }

    Schedule findSchedule(Long scheduleId){
        return scheduleRepository.findById(scheduleId).get();
    }

    MyDate findMyDate(User user, LocalDate date){
        return myDateRepository.findByUserAndAndDate(user, date).get();
    }


    @Test
    @DisplayName("스케줄을 등록하면 Schedule, MyDate, ScheduleHasMyDate 데이터베이스에 반영이 되어야 한다")
    void createSchedule() {
        Long userId = 1L;

        String title = "결혼식 참석";
        Color color = Color.FFADAD;
        LocalDateTime startDate = LocalDateTime.of(2023, 9, 23, 15, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 23, 19, 00, 00);

        ScheduleDTO.Request request = ScheduleDTO.Request.builder()
                .title(title)
                .color(color)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Schedule schedule  = scheduleService.create(userId, request);

        // Schedule
        assertThat(findSchedule(schedule.getId()).getId()).isEqualTo(userId);
        assertThat(findSchedule(schedule.getId()).getTitle()).isEqualTo(title);

        // Mydate
        assertThat(findMyDate(findUser(userId), startDate.toLocalDate()).getDate()).isEqualTo(startDate.toLocalDate());
        assertThat(findMyDate(findUser(userId), startDate.toLocalDate()).getUser().getId()).isEqualTo(userId);

    }
}
