package com.codeQuartette.myTime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ColorNotFoundException extends RuntimeException {

    public ColorNotFoundException() {
        super("해당 컬러를 찾을수 없습니다.");
    }
}
