package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.MyDateBulkRepository;
import com.codeQuartette.myTime.repository.MyDateRepository;
import com.codeQuartette.myTime.service.MyDateService;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyDateServiceImpl implements MyDateService {

    private final MyDateRepository myDateRepository;
    private final MyDateBulkRepository myDateBulkRepository;
    private final UserService userService;

    @Override
    public MyDate findMyDate(User user, LocalDate date) {
        return myDateRepository.findByUserAndDate(user, date)
                .orElseGet(() -> MyDate.builder()
                        .user(user)
                        .date(date)
                        .build());
    }

    public List<MyDate> findAllByUserId(Long userId) {
        User user = userService.findById(userId);
        return myDateRepository.findAllByUser(user);
    }

    //습관에 해당하는 모든 날짜 리스트로 리턴
    public List<LocalDate> checkAllDateByStartDateAndEndDate(LocalDate startDate, LocalDate endDate, String[] days) {
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if(Arrays.asList(days).contains(date.getDayOfWeek().toString().toUpperCase().substring(0, 3))) {
                dates.add(date);
            }
        }
        return dates;
    }

    /*
        습관에 해당하는 날짜 리스트를 유저가 이미 생성한 myDate리스트에 있는지 체크하고
        없는 날은 List에 담고, 있는 날은 id 값 가지고 List에 담아 리턴
     */
    public List<MyDate> validateDates(Long userId, List<LocalDate> habitDates) {
        User user = userService.findById(userId);
        List<MyDate> myDates = this.findAllByUserId(userId);
        List<MyDate> allHabitDates = habitDates.stream().map(date -> new MyDate(date, user)).toList(); //습관에 해당하는 모든 날짜

        if(myDates.size() != 0) {
            myDates.retainAll(allHabitDates); //교집합(ID를 가진 myDate리스트)
            allHabitDates.removeAll(myDates); //차집합

            myDates.addAll(allHabitDates);
        }
        else {
            myDates = allHabitDates;
        }

        return myDates;
    }

    public List<MyDate> saveAll(List<MyDate> myDates) {
        myDateBulkRepository.saveAll(myDates);

        return myDateRepository.findAllByDateInAndUser(myDates.stream().map(myDate -> myDate.getDate()).toList(), myDates.get(0).getUser());
    }

    public List<MyDate> saveAllMyDate(List<MyDate> myDates) {
        myDateBulkRepository.saveAllIgnore(myDates);
        return myDateRepository.findAllByDateInAndUser(myDates.stream().map(myDate -> myDate.getDate()).toList(), myDates.get(0).getUser());
    }


    @Override
    public MyDate save(MyDate myDate) {
        return myDateRepository.save(myDate);
    }

    @Override
    public MyDate find(User user, LocalDate date) {
        return myDateRepository.findByUserAndAndDate(user, date).orElseThrow(() -> new RuntimeException("해당하는 MyDate가 없습니다"));
    }

    @Override
    public boolean existMyDate(User user, LocalDate date) {
        return myDateRepository.findByUserAndAndDate(user, date).isPresent();
    }
}
