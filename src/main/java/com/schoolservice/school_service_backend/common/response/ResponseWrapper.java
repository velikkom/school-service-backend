package com.schoolservice.school_service_backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {

    private boolean success;
    private String message;
    private T data;

    /* =========================
       STATIC FACTORY METHODS
    ========================= */

    public static <T> ResponseWrapper<T> success(T data, String message) {
        return new ResponseWrapper<>(true, message, data);
    }

    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(true, null, data);
    }

    public static <T> ResponseWrapper<T> failure(String message) {
        return new ResponseWrapper<>(false, message, null);
    }

    public static <T> ResponseWrapper<T> fail(String message) {
        return new ResponseWrapper<>(false, message, null);
    }


    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(false, message, null);
    }


}
