package com.hoodie.otti.dto.login;

/**
 * HTTP 상태 코드, 메시지 및 데이터를 포함하는 일반적인 응답 객체입니다.
 *
 * @param <T> 데이터 필드의 유형
 */
public class BaseResponse<T> {
    private int statusCode; // HTTP 상태 코드
    private String message; // 응답 메시지
    private T data; // 응답 데이터

    /**
     * BaseResponse 객체를 생성합니다.
     *
     * @param statusCode HTTP 상태 코드
     * @param message    응답 메시지
     * @param data       응답 데이터
     */
    public BaseResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    /**
     * HTTP 상태 코드를 반환합니다.
     *
     * @return HTTP 상태 코드
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * HTTP 상태 코드를 설정합니다.
     *
     * @param statusCode 설정할 HTTP 상태 코드
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 응답 메시지를 반환합니다.
     *
     * @return 응답 메시지
     */
    public String getMessage() {
        return message;
    }

    /**
     * 응답 메시지를 설정합니다.
     *
     * @param message 설정할 응답 메시지
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 응답 데이터를 반환합니다.
     *
     * @return 응답 데이터
     */
    public T getData() {
        return data;
    }

    /**
     * 응답 데이터를 설정합니다.
     *
     * @param data 설정할 응답 데이터
     */
    public void setData(T data) {
        this.data = data;
    }
}
