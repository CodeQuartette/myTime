package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;

import java.time.LocalDate;

public interface MyDateService {

    MyDate save(MyDate myDate);

    MyDate find(User user, LocalDate date);

    boolean existMyDate(User user, LocalDate date);
}
