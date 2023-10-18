package com.codeQuartette.myTime.exception;

public class ToDoNotFoundException extends RuntimeException {

    public ToDoNotFoundException() {
        super("할 일이 없습니다");
    }
}
