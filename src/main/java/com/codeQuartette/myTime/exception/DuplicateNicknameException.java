package com.codeQuartette.myTime.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException() {
        super("중복 된 닉네임입니다.");
    }
}
