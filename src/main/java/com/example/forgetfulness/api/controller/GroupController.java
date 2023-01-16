package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.application.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;
}
