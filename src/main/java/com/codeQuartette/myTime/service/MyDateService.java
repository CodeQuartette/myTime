package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;

import java.time.LocalDate;

import java.util.List;

public interface MyDateService {

    MyDate findMyDateOrCreate(User user, LocalDate date);

    MyDate findMyDate(User user, LocalDate date);

    MyDate save(MyDate myDate);

    public List<MyDate> findAllByUserId(Long userId);

    public List<LocalDate> checkAllDateByStartDateAndEndDate(LocalDate startDate, LocalDate endDate, String[] days);

    public List<MyDate> validateDates(Long userId, List<LocalDate> habitDates);

    public List<MyDate> saveAll(List<MyDate> myDates);

    boolean existMyDate(User user, LocalDate date);

    List<MyDate> find(LocalDate date);

    List<MyDate> saveAllMyDate(List<MyDate> myDates);
}
