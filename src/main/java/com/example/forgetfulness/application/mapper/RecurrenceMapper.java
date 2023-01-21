package com.example.forgetfulness.application.mapper;

import com.example.forgetfulness.api.DTO.request.RecurrenceRequest;
import com.example.forgetfulness.application.entity.Recurrence;
import org.mapstruct.Mapper;

@Mapper
public interface RecurrenceMapper {
    Recurrence recurrenceRequestToRecurrence(RecurrenceRequest recurrenceRequest);
}
