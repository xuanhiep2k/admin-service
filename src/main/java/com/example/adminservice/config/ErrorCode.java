package com.example.adminservice.config;

public enum ErrorCode {
    SERVER_ERROR(500, "error.server_error"),
    NOT_FOUND(404, "error.not_found"),
    BAD_REQUEST(400, "error.bad_request"),
    UNAUTHORIZED(401, "error.unauthorized"),
    FORBIDDEN(401, "error.forbidden"),
    OK(200, "OK");

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
