package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.repository.HabitHasMyDateBulkRepository;
import com.codeQuartette.myTime.service.HabitHasMyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitHasMyMyDateServiceImpl implements HabitHasMyDateService {

    private final HabitHasMyDateBulkRepository habitHasDateBulkRepository;

    public void saveAll(List<HabitHasMyDate> habitHasMyDates) {
        habitHasDateBulkRepository.saveAll(habitHasMyDates);
    }
}
