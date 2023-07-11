package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.repository.HabitRepository;
import com.codeQuartette.myTime.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
}
