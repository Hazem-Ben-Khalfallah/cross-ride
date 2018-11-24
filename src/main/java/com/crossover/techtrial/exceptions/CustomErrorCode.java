package com.crossover.techtrial.exceptions;


import org.springframework.http.HttpStatus;

public enum CustomErrorCode {
    BAD_ARGS(HttpStatus.BAD_REQUEST, "Parameter should not be null or empty"), //
    DUPLICATE_IDENTIFIER(HttpStatus.CONFLICT, "An object with the same identifier already exists"), //
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Object not found"), //
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unmanaged exception"), //
    NOT_YET_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "Service not yet implemented"), //
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Requires user authentication"), //
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "Rights problem"), //
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable"), //
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "Problem with external api service");

    private final HttpStatus code;
    private final String message;

    CustomErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
