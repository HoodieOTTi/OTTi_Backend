package com.hoodie.otti.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private String message;
    private boolean success;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .message("Request was successful")
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> failure(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
