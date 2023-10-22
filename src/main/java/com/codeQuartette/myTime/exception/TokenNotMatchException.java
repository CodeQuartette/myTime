package com.codeQuartette.myTime.exception;


public class TokenNotMatchException extends RuntimeException {

    public TokenNotMatchException() {
        super("토큰이 일치하지 않습니다");
    }
}
