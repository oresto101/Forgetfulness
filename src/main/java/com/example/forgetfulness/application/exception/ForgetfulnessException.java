package com.example.forgetfulness.application.exception;

import lombok.Getter;

@Getter
public class ForgetfulnessException extends RuntimeException {
    private final ForgetfulnessExceptionType type;

    public ForgetfulnessException(ForgetfulnessExceptionType type) {
        super(type.getCode());
        this.type = type;
    }
}
