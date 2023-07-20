package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.MyDateRepository;
import com.codeQuartette.myTime.service.MyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MyDateServiceImpl implements MyDateService {

    private final MyDateRepository myDateRepository;

    @Override
    public MyDate findMyDate(User user, LocalDate date) {
        return myDateRepository.findByUserAndDate(user, date)
                .orElseGet(() -> MyDate.builder()
                        .user(user)
                        .date(date)
                        .build());
    }

    @Override
    public MyDate save(MyDate myDate) {
        return myDateRepository.save(myDate);
    }
}
