package com.example.forgetfulness.api.DTO.response;

import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.entity.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {
    private Long id;
    private String name;
    private String description;
    private Set<UserGroup> members;
    private Set<Reminder> reminders;
}
