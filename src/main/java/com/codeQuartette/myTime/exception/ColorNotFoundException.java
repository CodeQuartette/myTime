package com.codeQuartette.myTime.exception;

public class ColorNotFoundException extends RuntimeException {

    public ColorNotFoundException() {
        super("해당 컬러를 찾을수 없습니다.");
    }
}
