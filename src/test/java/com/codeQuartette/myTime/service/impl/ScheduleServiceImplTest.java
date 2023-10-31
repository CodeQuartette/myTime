package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.config.RedisConfig;
import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.ScheduleHasMyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.domain.value.Color;
import com.codeQuartette.myTime.exception.ScheduleNotFoundException;
import com.codeQuartette.myTime.repository.MyDateRepository;
import com.codeQuartette.myTime.repository.ScheduleHasMyDateRepository;
import com.codeQuartette.myTime.repository.ScheduleRepository;
import com.codeQuartette.myTime.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
@ExtendWith(RedisConfig.class)
@SpringBootTest
class ScheduleServiceImplTest {

    @Autowired
     private ScheduleServiceImpl scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MyDateRepository myDateRepository;

    @Autowired
    private ScheduleHasMyDateRepository scheduleHasMyDateRepository;

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

    List<ScheduleHasMyDate> findScheduleHasMyDate(Schedule schedule){
        return scheduleHasMyDateRepository.findBySchedule(schedule);
    }

    // 스케줄 등록
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
                .isSpecificTime(false)
                .alert(true)
                .build();

        Schedule schedule  = scheduleService.create(userId, request);

        // Schedule
        assertThat(findSchedule(schedule.getId()).getId()).isEqualTo(schedule.getId());
        assertThat(findSchedule(schedule.getId()).getTitle()).isEqualTo(title);

        // Mydate
        assertThat(findMyDate(findUser(userId), startDate.toLocalDate()).getDate()).isEqualTo(startDate.toLocalDate());
        assertThat(findMyDate(findUser(userId), startDate.toLocalDate()).getUser().getId()).isEqualTo(userId);

        // ScheduleHasDate
        assertThat(findScheduleHasMyDate(schedule).get(0)).isNotNull();
    }


    @Test
    @DisplayName("스케줄을 등록하면 9월 23일 ~ 9월 25일까지 ScheduleHasMyDate List의 Size 3이 반영되어야한다.")
    void createSchedule_date() {
        Long userId = 1L;

        String title = "결혼식 참석";
        Color color = Color.FFADAD;
        LocalDateTime startDate = LocalDateTime.of(2023, 9, 23, 15, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 25, 19, 00, 00);

        ScheduleDTO.Request request = ScheduleDTO.Request.builder()
                .title(title)
                .color(color)
                .startDate(startDate)
                .endDate(endDate)
                .isSpecificTime(false)
                .alert(true)
                .build();

        Schedule schedule  = scheduleService.create(userId, request);

        // ScheduleHasDate
        assertThat(findScheduleHasMyDate(schedule).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("스케줄을 수정하면 반영되어야한다.")
    void updateSchedule() {

        // given
        Long userId = 1L;

        String title = "결혼식 참석";
        Color color = Color.FFADAD;
        LocalDateTime startDate = LocalDateTime.of(2023, 9, 23, 15, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 25, 19, 00, 00);

        ScheduleDTO.Request create = ScheduleDTO.Request.builder()
                .title(title)
                .color(color)
                .startDate(startDate)
                .endDate(endDate)
                .isSpecificTime(false)
                .alert(true)
                .build();

        Schedule schedule  = scheduleService.create(userId, create);

        //when
        String updatedTitle = "과일가게 방문";
        Color updatedColor = Color.BDB2FF;
        LocalDateTime updatedStartDate = LocalDateTime.of(2023, 9, 26, 15, 00, 00);
        LocalDateTime updatedEndDate = LocalDateTime.of(2023, 9, 26, 19, 00, 00);


        ScheduleDTO.Request update = ScheduleDTO.Request.builder()
                .title(updatedTitle)
                .color(updatedColor)
                .startDate(updatedStartDate)
                .endDate(updatedEndDate)
                .build();


        Schedule updatedSchedule = scheduleService.update(userId, schedule.getId(), update);

        //then
        assertThat(updatedSchedule.getId()).isEqualTo(schedule.getId());
        assertThat(updatedSchedule.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedSchedule.getColor()).isEqualTo(updatedColor);
        assertThat(updatedSchedule.getStartDateTime()).isEqualTo(updatedStartDate);
        assertThat(updatedSchedule.getEndDateTime()).isEqualTo(updatedEndDate);
    }

    @Test
    @DisplayName("스케줄을 삭제하면 schedule을 찾을 때 Exception으로 결과가 되어야한다")
    void deleteSchedule() {

        // given
        Long userId = 1L;

        String title = "결혼식 참석";
        Color color = Color.FFADAD;
        LocalDateTime startDate = LocalDateTime.of(2023, 9, 23, 15, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2023, 9, 25, 19, 00, 00);

        ScheduleDTO.Request create = ScheduleDTO.Request.builder()
                .title(title)
                .color(color)
                .startDate(startDate)
                .endDate(endDate)
                .isSpecificTime(false)
                .alert(true)
                .build();

        Schedule schedule  = scheduleService.create(userId, create);

        //when
        scheduleService.delete(userId, schedule.getId());

        //then
        assertThatThrownBy(() -> scheduleService.find(userId, schedule.getId()))
                .isInstanceOf(ScheduleNotFoundException.class);
    }
}
