package com.codeQuartette.myTime.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenNotMatchException extends RuntimeException {

    public TokenNotMatchException() {
        super("토큰이 일치하지 않습니다");
    }
}
