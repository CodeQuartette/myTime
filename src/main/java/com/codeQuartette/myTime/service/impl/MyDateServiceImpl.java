package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;
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
    private final UserService userService;

    public List<MyDate> findAllByUserId(Long userId) {
        User user = userService.findById(userId);
        return myDateRepository.findAllByUser(user);
    }

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
        없는 날만 List에 담아서 리턴
     */
    public List<MyDate> validateDates(Long userId, List<LocalDate> habitDates) {
        User user = userService.findById(userId);
        List<MyDate> myDates = this.findAllByUserId(userId);
        List<LocalDate> convertMyDates = myDates.stream().map(myDate -> myDate.getDate()).toList();
        habitDates.removeAll(convertMyDates);
        return habitDates.stream().map(date -> MyDate.builder().date(date).user(user).build()).toList();
    }

    public void saveAll(List<MyDate> myDates) {
        myDateRepository.saveAll(myDates);
    }
}
