package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.response.ReminderResponse;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.mapper.ReminderMapper;
import com.example.forgetfulness.application.service.ReminderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reminder")
@AllArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;
    private final ReminderMapper reminderMapper;

    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAllReminders() {
        List<ReminderResponse> list = reminderService
                .getAllReminders()
                .stream()
                .map(reminderMapper::reminderToReminderResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponse> getReminderById(@PathVariable("id") Long id) {
        Optional<Reminder> reminderById = reminderService.getReminderById(id);

        return reminderById
                .map(reminder ->
                        ResponseEntity
                                .status(HttpStatus.OK)
                                .body(reminderMapper.reminderToReminderResponse(reminder)))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @PostMapping
    public ResponseEntity<ReminderResponse> createReminder(@RequestBody ReminderRequest reminderRequest) {
        ReminderResponse reminderResponse = reminderMapper.reminderToReminderResponse(
                reminderService.save(
                        reminderMapper.reminderRequestToReminder(reminderRequest)));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reminderResponse);
    }

    @PutMapping
    public ResponseEntity<ReminderResponse> updateReminder(@RequestBody ReminderRequest reminderRequest) {
        ReminderResponse reminderResponse = reminderMapper.reminderToReminderResponse(
                reminderService.update(
                        reminderMapper.reminderRequestToReminder(reminderRequest)));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reminderResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable("id") Long id) {
        reminderService.delete(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
