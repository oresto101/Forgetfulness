package com.example.forgetfulness;


import com.example.forgetfulness.api.DTO.request.UserRequest;
import com.example.forgetfulness.api.DTO.response.UserResponse;
import com.example.forgetfulness.api.controller.UserController;
import com.example.forgetfulness.application.entity.Reminder;
import com.example.forgetfulness.application.entity.User;
import com.example.forgetfulness.application.entity.UserGroup;
import com.example.forgetfulness.application.mapper.UserMapper;
import com.example.forgetfulness.application.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.ResponseEntity.BodyBuilder;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        Set<Reminder> reminders1 = new HashSet<>();
        Set<UserGroup> groups1 = new HashSet<>();
        User user1 = new User(1L, "password", "name1", "surname1", "example@gmail.com", groups1, reminders1);
        Set<Reminder> reminders2 = new HashSet<>();
        Set<UserGroup> groups2 = new HashSet<>();
        User user2 = new User(1L, "password", "name2", "surname2", "example@gmail.com", groups2, reminders2);
        List<User> users = Arrays.asList(user1, user2);
        UserResponse userResponse1 = new UserResponse(1L, "name1", "username1", "surname1", "email1");
        UserResponse userResponse2 = new UserResponse(2L, "name2", "username2", "surname2", "email2");
        List<UserResponse> userResponses = Arrays.asList(userResponse1, userResponse2);

        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.userToUserResponse(user1)).thenReturn(userResponse1);
        when(userMapper.userToUserResponse(user2)).thenReturn(userResponse2);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("name2"));
    }

    @Test
    public void testGetUserById() throws Exception {
        Set<Reminder> reminders = new HashSet<>();
        Set<UserGroup> groups = new HashSet<>();
        User user = new User(1L, "password", "name1", "surname1", "example@gmail.com", groups, reminders);
        UserResponse userResponse = new UserResponse(1L, "name1", "username1", "surname1", "email1");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.surname").value("surname1"))
                .andExpect(jsonPath("$.email").value("email1"));
    }
    @Test
    public void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest(1L, "password1", "name1", "username1", "surname1", "email1");
        Set<Reminder> reminders = new HashSet<>();
        Set<UserGroup> groups = new HashSet<>();
        User user = new User(1L, "password", "name1", "surname1", "example@gmail.com", groups, reminders);
        UserResponse userResponse = new UserResponse(1L, "name1", "username1", "surname1", "email1");

        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(userRequest);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.surname").value("surname1"))
                .andExpect(jsonPath("$.email").value("email1"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserRequest userRequest = new UserRequest(1L, "password1", "name1", "username1", "surname1", "email1");
        Set<Reminder> reminders = new HashSet<>();
        Set<UserGroup> groups = new HashSet<>();
        User user = new User(1L, "password", "name1", "surname1", "example@gmail.com", groups, reminders);
        UserResponse userResponse = new UserResponse(1L, "name1", "username1", "surname1", "email1");
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userService.update(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(userRequest);


        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.username").value("username1"))
                .andExpect(jsonPath("$.surname").value("surname1"))
                .andExpect(jsonPath("$.email").value("email1"));
    }



    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/1"))
                .andExpect(status().isOk());
    }


}
