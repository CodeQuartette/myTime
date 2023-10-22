package com.codeQuartette.myTime.exception;

public class UserNotMatchException extends RuntimeException {

    public UserNotMatchException() {
        super("유저가 일치하지 않습니다");
    }
}
