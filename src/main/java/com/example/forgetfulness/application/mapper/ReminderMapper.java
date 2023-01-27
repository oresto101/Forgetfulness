package com.example.forgetfulness.application.mapper;

import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.response.ReminderResponse;
import com.example.forgetfulness.application.entity.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReminderMapper {
    @Mapping(source = "period", target = "recurrence.period")
    @Mapping(source = "time", target = "recurrence.time")
    Reminder reminderRequestToReminder(ReminderRequest reminderRequest);

    @Mapping(source = "recurrence.period", target = "period")
    @Mapping(source = "recurrence.time", target = "time")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "group.id", target = "groupId")
    ReminderResponse reminderToReminderResponse(Reminder reminder);
}
