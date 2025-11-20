package com.lei.usercenter.exception;

/**
 * 资未找到异常
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}