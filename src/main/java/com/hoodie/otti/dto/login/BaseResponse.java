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

    /**
     * 객체 정보를 문자열로 반환합니다.
     *
     * @return 객체 정보 문자열
     */
    @Override
    public String toString() {
        return "BaseResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 객체 동등성 비교를 위한 equals 메서드입니다.
     *
     * @param o 비교할 객체
     * @return 동등성 여부
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseResponse<?> that = (BaseResponse<?>) o;

        if (statusCode != that.statusCode) return false;
        if (!message.equals(that.message)) return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    /**
     * 객체 해시코드를 생성합니다.
     *
     * @return 해시코드
     */
    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + message.hashCode();
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
