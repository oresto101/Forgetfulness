package com.example.forgetfulness.api.user;

import com.example.forgetfulness.application.user.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> addUser(@RequestBody final CreateUserRequest createUserRequest){
        userService.addUser(createUserRequest);
        return ResponseEntity.accepted().body("User added");
    }
}
