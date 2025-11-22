package com.lei.mall.exception;

import com.lei.mall.common.ErrorCode;
import lombok.Getter;

/**
 * 业务异常
 * @author lei
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCode.PARAMS_ERROR.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

}