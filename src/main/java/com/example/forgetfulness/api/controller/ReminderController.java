package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.application.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService service;
}
