package com.codeQuartette.myTime.exception;

public class ScheduleNotFoundException extends RuntimeException {

    public ScheduleNotFoundException() {
        super("해당 스케줄을 찾을수 없습니다.");
    }
}
