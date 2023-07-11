package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl {

    private final HabitRepository habitRepository;
}
