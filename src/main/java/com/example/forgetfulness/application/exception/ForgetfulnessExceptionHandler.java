package com.example.forgetfulness.application.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
@Component
public class ForgetfulnessExceptionHandler extends ResponseEntityExceptionHandler {
    private final ForgetfulnessErrorMapper forgetfulnessErrorMapper;

    @ExceptionHandler(value = {ForgetfulnessException.class})
    protected ResponseEntity<Object> handleForgetfulnessException(ForgetfulnessException ex, WebRequest request) {
        var error = forgetfulnessErrorMapper.forgetfulnessEexceptionToForgetfulnessError(ex, request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<Object>(
                error.toJson(), headers, error.getStatus());
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        return handleForgetfulnessException(new ForgetfulnessException((ForgetfulnessExceptionType.INTERNAL)), request);
    }
}
