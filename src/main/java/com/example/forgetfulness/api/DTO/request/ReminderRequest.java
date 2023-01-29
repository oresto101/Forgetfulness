package com.example.forgetfulness.api.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequest {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Long period;
    private LocalTime time;
}
