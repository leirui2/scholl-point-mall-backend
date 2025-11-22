package com.lei.mall.exception;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author lei
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有未捕获的异常
     * 去掉@ResponseStatus注解后，返回的状态码会是200
     */
    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception e) {
        log.error("系统内部异常", e);
        return new ApiResponse<>(ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * 处理特定业务异常
     * 去掉@ResponseStatus注解后，返回的状态码会是200
     */
    @ExceptionHandler(BusinessException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBusinessException(BusinessException e) {
        return new ApiResponse<>(ErrorCode.PARAMS_ERROR.getCode(),
                e.getMessage());
    }

    /**
     * 处理资源未找到异常
     * 去掉@ResponseStatus注解后，返回的状态码会是200
     */
    @ExceptionHandler(ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ApiResponse<>(ErrorCode.NOT_FOUND_ERROR.getCode(),
                e.getMessage());
    }
}
