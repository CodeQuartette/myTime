package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;

import java.time.LocalDate;
import java.util.List;

public interface MyDateService {

    public List<MyDate> findAllByUserId(Long userId);

    public List<LocalDate> checkAllDateByStartDateAndEndDate(LocalDate startDate, LocalDate endDate, String[] days);

    public List<MyDate> validateDates(Long userId, List<LocalDate> habitDates);

    public List<MyDate> saveAll(List<MyDate> myDates);


    MyDate save(MyDate myDate);

    MyDate find(User user, LocalDate date);

    boolean existMyDate(User user, LocalDate date);
}
