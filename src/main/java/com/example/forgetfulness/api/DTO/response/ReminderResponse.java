package com.example.forgetfulness.api.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReminderResponse {
    private Long id;
    private String name;
    private String description;
    private String date;
    private Long period;
}
