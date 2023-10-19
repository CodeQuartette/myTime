package com.codeQuartette.myTime.controller.globalResponse;

import com.codeQuartette.myTime.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({UserNotFoundException.class, ScheduleNotFoundException.class, HabitNotFoundException.class,
            ColorNotFoundException.class, ToDoNotFoundException.class})
    public ResponseDTO<?> notFoundException(Exception e) {
        log.info("e: {}", e.getMessage());

        return ResponseDTO.exception(ResponseType.NOT_FOUND, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({DuplicateUserException.class, DuplicateNicknameException.class})
    public ResponseDTO<?> duplicateException(Exception e) {
        log.info("e: {}", e.getMessage());

        return ResponseDTO.exception(ResponseType.BAD_REQUEST, e.getMessage());
    }
}
