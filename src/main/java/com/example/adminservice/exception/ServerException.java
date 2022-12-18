package com.example.adminservice.exception;

import com.example.adminservice.config.ErrorCode;
import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {
    private String message;
    private ErrorCode errorCode;
    private Object payload;
    private String[] args;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServerException(ErrorCode errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public ServerException(ErrorCode errorCode, String... args) {
        this.errorCode = errorCode;
        this.args = args;
    }

    public ServerException(Object payload) {
        this.payload = payload;
    }
}
