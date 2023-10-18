package com.codeQuartette.myTime.controller.globalResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {

    private Integer code;
    private String message;

    @Nullable
    T response;

    public static <T> ResponseDTO<T> from(ResponseType responseType) {
        return new ResponseDTO<>(responseType.getCode(), responseType.getMessage(), null);
    }

    public static <T> ResponseDTO<T> from(ResponseType responseType, @Nullable final T response) {
        return new ResponseDTO<>(responseType.getCode(), responseType.getMessage(), response);
    }

    public static <T> ResponseDTO<T> exception(ResponseType responseType, String message) {
        return new ResponseDTO<>(responseType.getCode(), message, null);
    }
}
