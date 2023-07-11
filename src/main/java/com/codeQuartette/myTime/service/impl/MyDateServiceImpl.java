package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.repository.MyDateRepository;
import com.codeQuartette.myTime.service.MyDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyDateServiceImpl implements MyDateService {

    private final MyDateRepository myDateRepository;
}
