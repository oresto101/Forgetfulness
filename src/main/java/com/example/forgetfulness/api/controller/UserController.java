package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
}
