package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.HabitHasDate;
import com.codeQuartette.myTime.repository.HabitHasDateBulkRepository;
import com.codeQuartette.myTime.service.HabitHasDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitHasDateServiceImpl implements HabitHasDateService {

    private final HabitHasDateBulkRepository habitHasDateBulkRepository;

    public void saveAll(List<HabitHasDate> habitHasDates) {
        habitHasDateBulkRepository.saveAll(habitHasDates);
    }
}
