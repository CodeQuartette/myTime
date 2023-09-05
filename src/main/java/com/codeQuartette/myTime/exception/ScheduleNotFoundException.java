package com.codeQuartette.myTime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScheduleNotFoundException extends RuntimeException {

    public ScheduleNotFoundException() {
        super("해당 스케쥴을 찾을수 없습니다.");
    }
}
