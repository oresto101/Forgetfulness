package com.example.forgetfulness.api.DTO.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecurrenceRequest {
    private Long id;
    private Long period;
    private Long reminderId;
}
