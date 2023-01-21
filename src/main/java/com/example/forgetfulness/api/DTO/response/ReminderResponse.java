package com.example.forgetfulness.api.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReminderResponse {
    private String name;
    private String description;
    private LocalDate date;
    private Long period;
}
