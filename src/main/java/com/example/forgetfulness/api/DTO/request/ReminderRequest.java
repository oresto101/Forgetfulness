package com.example.forgetfulness.api.DTO.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReminderRequest {
    private Long id;
    private String name;
    private String description;
    private Long period;
}
