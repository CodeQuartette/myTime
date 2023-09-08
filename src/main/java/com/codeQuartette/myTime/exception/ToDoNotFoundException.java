package com.codeQuartette.myTime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ToDoNotFoundException extends RuntimeException {

    public ToDoNotFoundException() { super("할 일을 조회 할 수 없습니다");
    }
}
