package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.repository.ScheduleRepository;
import com.codeQuartette.myTime.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
}
