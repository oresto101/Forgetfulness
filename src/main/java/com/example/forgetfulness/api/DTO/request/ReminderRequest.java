package com.example.forgetfulness.api.DTO.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReminderRequest {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Long period;
}
