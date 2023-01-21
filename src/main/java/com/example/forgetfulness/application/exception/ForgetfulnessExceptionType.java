package com.example.forgetfulness.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ForgetfulnessExceptionType {
    INTERNAL("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    ID_PROBLEM("Id problem", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String description;
    private final HttpStatus status;

    ForgetfulnessExceptionType(String description, HttpStatus status) {
        this.code = this.name();
        this.description = description;
        this.status = status;
    }
}
