package com.example.forgetfulness.application.recurrence;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecurrenceService {

    private final RecurrenceRepository recurrenceRepository;
}
