package com.lei.usercenter.exception;

import java.io.Serializable;

/**
 * 异常响应实体
 */
public class ErrorResponse implements Serializable {
    private String message;
    private int code;

    public ErrorResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    // getter and setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
