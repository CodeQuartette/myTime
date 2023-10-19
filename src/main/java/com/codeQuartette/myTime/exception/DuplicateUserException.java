package com.codeQuartette.myTime.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException() {
        super("중복 된 유저입니다.");
    }
}
