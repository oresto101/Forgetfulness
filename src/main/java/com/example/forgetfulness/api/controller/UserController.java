package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.request.UserRequest;
import com.example.forgetfulness.api.DTO.response.GroupResponse;
import com.example.forgetfulness.api.DTO.response.ReminderResponse;
import com.example.forgetfulness.api.DTO.response.UserResponse;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.mapper.GroupMapper;
import com.example.forgetfulness.application.mapper.ReminderMapper;
import com.example.forgetfulness.application.mapper.UserMapper;
import com.example.forgetfulness.application.service.UserGroupService;
import com.example.forgetfulness.application.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserGroupService userGroupService;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;
    private final ReminderMapper reminderMapper;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> list = userService
                .getAllUsers()
                .stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        Optional<User> userById = userService.getUserById(id);

        return userById
                .map(user ->
                        ResponseEntity
                                .status(HttpStatus.OK)
                                .body(userMapper.userToUserResponse(user)))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }


    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userMapper.userToUserResponse(
                userService.save(
                        userMapper.userRequestToUser(userRequest)));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest userRequest) {
        Optional<User> userByEmail = userService.getUserByEmail(userRequest.getEmail());
        if (userByEmail.isEmpty()) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.NO_USER);
        }

        if (!userByEmail.get().getPassword().equals(userRequest.getPassword())) {
            throw new ForgetfulnessException(ForgetfulnessExceptionType.INCORRECT_CREDENTIALS);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userMapper.userToUserResponse(userByEmail.get()));
    }


    @PutMapping("/{userId}/add/group/{groupId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        userGroupService.save(userId, groupId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{userId}/delete/group/{groupId}")
    public ResponseEntity<String> deleteUserFromGroup(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        userGroupService.delete(userId, groupId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{userId}/groups")
    public ResponseEntity<List<GroupResponse>> getUserGroups(@PathVariable("userId") Long userId) {
        List<GroupResponse> list = userGroupService
                .getUserGroups(userId)
                .stream()
                .map(groupMapper::groupToGroupResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }


    @PostMapping("/{userId}/add/reminder")
    public ResponseEntity<ReminderResponse> addReminderToUser(@PathVariable("userId") Long userid, @RequestBody ReminderRequest reminderRequest) {
        Reminder reminder = userService.addReminder(userid, reminderMapper.reminderRequestToReminder(reminderRequest));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reminderMapper.reminderToReminderResponse(reminder));
    }

    @DeleteMapping("/{userId}/delete/reminder/{reminderId}")
    public ResponseEntity<String> deleteReminderFromUser(@PathVariable("userId") Long userid, @PathVariable("reminderId") Long reminderId) {
        userService.deleteReminder(userid, reminderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @GetMapping("/{userId}/reminders")
    public ResponseEntity<List<ReminderResponse>> getUserReminders(@PathVariable("userId") Long userId) {
        List<ReminderResponse> list = userService
                .getUserReminders(userId)
                .stream()
                .map(reminderMapper::reminderToReminderResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }
}
