package com.example.forgetfulness.application.service;

import com.example.forgetfulness.application.repository.RecurrenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecurrenceService {

    private final RecurrenceRepository recurrenceRepository;
}
