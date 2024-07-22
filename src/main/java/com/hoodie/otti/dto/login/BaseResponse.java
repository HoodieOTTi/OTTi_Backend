package com.hoodie.otti.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 응답의 기본 구조를 정의하는 클래스입니다.
 * 모든 응답에서 공통으로 사용되는 필드와 메서드를 제공합니다.
 *
 * @param <T> 응답 데이터의 타입
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private String message;   // 응답 메시지
    private boolean success;  // 요청 처리 성공 여부
    private T data;           // 응답 데이터

    /**
     * 성공적인 응답을 생성하는 헬퍼 메서드입니다.
     *
     * @param data 응답 데이터
     * @param <T>  응답 데이터의 타입
     * @return 성공적인 응답
     */
    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .message("Request was successful")
                .data(data)
                .build();
    }

    /**
     * 실패 응답을 생성하는 헬퍼 메서드입니다.
     *
     * @param message 오류 메시지
     * @param <T>     응답 데이터의 타입
     * @return 실패 응답
     */
    public static <T> BaseResponse<T> failure(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
