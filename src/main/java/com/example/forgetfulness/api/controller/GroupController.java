package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.api.DTO.request.GroupRequest;
import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.response.GroupResponse;
import com.example.forgetfulness.api.DTO.response.ReminderResponse;
import com.example.forgetfulness.api.DTO.response.UserResponse;
import com.example.forgetfulness.application.entity.Group;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.mapper.GroupMapper;
import com.example.forgetfulness.application.mapper.ReminderMapper;
import com.example.forgetfulness.application.mapper.UserMapper;
import com.example.forgetfulness.application.service.GroupService;
import com.example.forgetfulness.application.service.UserGroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final UserGroupService userGroupService;
    private final GroupMapper groupMapper;
    private final ReminderMapper reminderMapper;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> list = groupService
                .getAllGroups()
                .stream()
                .map(groupMapper::groupToGroupResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable("id") Long id) {
        Optional<Group> groupById = groupService.getGroupById(id);

        return groupById
                .map(group ->
                        ResponseEntity
                                .status(HttpStatus.OK)
                                .body(groupMapper.groupToGroupResponse(group)))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }


    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest groupRequest) {
        GroupResponse groupResponse = groupMapper.groupToGroupResponse(
                groupService.save(
                        groupMapper.groupRequestToGroup(groupRequest)));


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable("id") Long id) {
        groupService.delete(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<UserResponse>> getGroupUsers(@PathVariable("groupId") Long groupId) {
        List<UserResponse> list = userGroupService
                .getGroupUsers(groupId)
                .stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }


    @PostMapping("/{groupId}/add/reminder")
    public ResponseEntity<ReminderResponse> addReminderToGroup(@PathVariable("groupId") Long groupId, @RequestBody ReminderRequest reminderRequest) {
        Reminder reminder = groupService.addReminder(groupId, reminderMapper.reminderRequestToReminder(reminderRequest));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reminderMapper.reminderToReminderResponse(reminder));
    }

    @DeleteMapping("/{groupId}/delete/reminder/{reminderId}")
    public ResponseEntity<String> deleteReminderFromGroup(@PathVariable("groupId") Long groupid, @PathVariable("reminderId") Long reminderId) {
        groupService.deleteReminder(groupid, reminderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{groupId}/reminders")
    public ResponseEntity<List<ReminderResponse>> getGroupReminders(@PathVariable("groupId") Long groupId) {
        List<ReminderResponse> list = groupService
                .getGroupReminders(groupId)
                .stream()
                .map(reminderMapper::reminderToReminderResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }
}
