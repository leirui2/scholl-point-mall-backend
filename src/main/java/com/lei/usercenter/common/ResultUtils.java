package com.lei.usercenter.common;

/**
 * 返回工具类
 * @author lei
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ErrorCode.SUCCESS.getCode(), "ok", data);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static ApiResponse error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static ApiResponse error(int code, String message) {
        return new ApiResponse<>(code, message);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static ApiResponse error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode.getCode(), message);
    }
}
