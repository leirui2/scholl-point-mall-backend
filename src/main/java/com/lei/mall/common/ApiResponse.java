package com.lei.mall.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 标准响应实体
 * @author lei
 */
@Data
public class ApiResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
