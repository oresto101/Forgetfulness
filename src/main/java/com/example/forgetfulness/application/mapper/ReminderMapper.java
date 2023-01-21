package com.example.forgetfulness.application.mapper;

import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.response.ReminderResponse;
import com.example.forgetfulness.application.entity.Reminder;
import org.mapstruct.Mapper;

@Mapper
public interface ReminderMapper {
    Reminder reminderRequestToReminder(ReminderRequest reminderRequest);

    ReminderResponse reminderToReminderResponse(Reminder reminder);
}
