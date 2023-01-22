package com.example.forgetfulness.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ForgetfulnessExceptionType {
    INTERNAL("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    ID_PROBLEM("Id problem", HttpStatus.BAD_REQUEST),

    NO_USER("No user with such id", HttpStatus.NOT_FOUND),
    NO_GROUP("No group with such id", HttpStatus.NOT_FOUND),
    NO_REMINDER("No reminder with such id", HttpStatus.NOT_FOUND),

    USER_ALREADY_EXISTS("User with such id already exists", HttpStatus.CONFLICT),
    GROUP_ALREADY_EXISTS("User with such id already exists", HttpStatus.CONFLICT),
    REMINDER_ALREADY_EXISTS("User with such id already exists", HttpStatus.CONFLICT);


    private final String code;
    private final String description;
    private final HttpStatus status;

    ForgetfulnessExceptionType(String description, HttpStatus status) {
        this.code = this.name();
        this.description = description;
        this.status = status;
    }
}
