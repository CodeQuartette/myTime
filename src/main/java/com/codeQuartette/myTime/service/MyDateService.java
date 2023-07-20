package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;

import java.time.LocalDate;

public interface MyDateService {

    MyDate findMyDate(User user, LocalDate date);

    MyDate save(MyDate myDate);
}
