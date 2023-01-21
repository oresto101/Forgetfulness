package com.example.forgetfulness.api.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecurrenceResponse {
    private Long period;
    private Long reminderId;
}
