package com.example.forgetfulness.application.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ForgetfulnessErrorMapper {
    ForgetfulnessError forgetfulnessEexceptionToForgetfulnessError(ForgetfulnessException ex, WebRequest request) {
        ServletWebRequest webRequest = (ServletWebRequest) request;
        return new ForgetfulnessError(
                ex.getType().getCode(),
                ex.getType().getDescription(),
                webRequest.getRequest().getRequestURI(),
                LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                ex.getType().getStatus()
        );
    }
}
