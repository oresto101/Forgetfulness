package com.example.forgetfulness.api.controller;

import com.example.forgetfulness.api.DTO.request.UserRequest;
import com.example.forgetfulness.api.DTO.response.UserResponse;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.exception.ForgetfulnessException;
import com.example.forgetfulness.application.exception.ForgetfulnessExceptionType;
import com.example.forgetfulness.application.mapper.UserMapper;
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
    private final UserMapper userMapper;

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
}
