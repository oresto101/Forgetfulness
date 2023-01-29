package com.example.forgetfulness.application.controller;

import com.example.forgetfulness.api.DTO.request.ReminderRequest;
import com.example.forgetfulness.api.DTO.request.UserRequest;
import com.example.forgetfulness.application.entity.*;
import com.example.forgetfulness.application.mapper.UserMapper;
import com.example.forgetfulness.application.repository.GroupRepository;
import com.example.forgetfulness.application.repository.ReminderRepository;
import com.example.forgetfulness.application.repository.UserGroupRepository;
import com.example.forgetfulness.application.repository.UserRepository;
import com.example.forgetfulness.application.service.UserGroupService;
import com.example.forgetfulness.application.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.forgetfulness.application.controller.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


import java.util.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Test
    public void shouldReturnListOfUsersGivenDBContainsAtLeastOneUser() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);

        //when
        final MvcResult result = mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[{\"id\":1,\"name\":\"name\",\"surname\":\"surname\",\"email\":\"email\"}]");
   }

    @Test
    public void shouldReturnEmptyListGivenDBContainsNoUsers() throws Exception {
        //given

        //when
        final MvcResult result = mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[]");
    }

    @Test
    public void shouldReturnUserByIdGivenUserExists() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        //when
        final MvcResult result = mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"name\",\"surname\":\"surname\",\"email\":\"email\"}");
    }

    @Test
    public void shouldReturnNotFoundGivenUserDoesntExist() throws Exception {
        //given

        //when then
        final MvcResult result = mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void shouldCreateUserGivenEmailIsUnique() throws Exception {
        //given
        final UserRequest userRequest = new UserRequest(null, PASSWORD, NAME, SURNAME, EMAIL);

        //when
        final MvcResult result = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();
        final User addedUser = users.get(0);
        assertEquals(addedUser.getPassword(), PASSWORD);
        assertEquals(addedUser.getName(), NAME);
        assertEquals(addedUser.getSurname(), SURNAME);
        assertEquals(addedUser.getEmail(), EMAIL);
        final String userResponseString = result.getResponse().getContentAsString();
        assertEquals(userResponseString, "{\"id\":1,\"name\":\"name\",\"surname\":\"surname\",\"email\":\"email\"}");
    }

    @Test
    public void shouldReturnConflictGivenUserWithEmailExistsAlready() throws Exception {
        //given
        final User userWithSameEmail = new User(1L, PASSWORD_1, NAME_1, SURNAME_1, EMAIL, Set.of(), List.of());
        userRepository.save(userWithSameEmail);
        final UserRequest userRequest = new UserRequest(null, PASSWORD, NAME, SURNAME, EMAIL);

        //when
        final MvcResult result = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict())
                .andReturn();

        //then
        final String userResponseString = result.getResolvedException().getMessage();
        assertEquals(userResponseString, "USER_EMAIL_ALREADY_EXISTS");
    }

    @Test
    public void shouldDeleteUserWhenUserExists() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);

        //when
        final MvcResult result = mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<User> users = userRepository.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    public void shouldAllowUserToLoginGivenUserExistsAndCredentialsAreCorrect() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final UserRequest loginRequest = new UserRequest(null, PASSWORD, null, null, EMAIL);

        //when
        final MvcResult result = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String response = result.getResponse().getContentAsString();
        assertEquals(response, "{\"id\":1,\"name\":\"name\",\"surname\":\"surname\",\"email\":\"email\"}");
    }

    @Test
    public void shouldReturnNotFoundGivenEmailNotInDB() throws Exception {
        //given
        final UserRequest loginRequest = new UserRequest(null, PASSWORD, null, null, EMAIL);

        //when
        final MvcResult result = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "NO_USER");
    }

    @Test
    public void shouldReturnNotFoundGivenPasswordIncorrect() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final UserRequest loginRequest = new UserRequest(null, PASSWORD_1, null, null, EMAIL);

        //when
        final MvcResult result = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "INCORRECT_CREDENTIALS");
    }

    @Test
    public void shouldAddUserToGroupGivenUserExistsAndGroupExists() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final Group group = getGroup();
        groupRepository.save(group);

        //when
        final MvcResult result = mockMvc.perform(put("/user/1/add/group/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<UserGroup> resultingUserGroups = userGroupRepository.findAll();
        assertThat(resultingUserGroups).isNotEmpty();
        final UserGroup resultingUserGroup = resultingUserGroups.get(0);
        assertEquals(resultingUserGroup.getGroup().getName(), GROUP_NAME);
        assertEquals(resultingUserGroup.getGroup().getDescription(), GROUP_DESCRIPTION);
        assertEquals(resultingUserGroup.getUser().getName(), NAME);
        assertEquals(resultingUserGroup.getUser().getEmail(), EMAIL);
        assertEquals(resultingUserGroup.getUser().getSurname(), SURNAME);
    }

    @Test
    public void shouldReturnNotFoundGivenUserDoesntExistAndGroupExists() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);

        //when
        final MvcResult result = mockMvc.perform(put("/user/1/add/group/1"))
                .andExpect(status().isNotFound())
                .andReturn();

        //then

        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "NO_GROUP");
    }

    @Test
    public void shouldReturnConflictGivenUserInGroup() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final Group group = getGroup();
        groupRepository.save(group);
        userGroupService.save(1L, 1L);

        //when
        final MvcResult result = mockMvc.perform(put("/user/1/add/group/1"))
                .andExpect(status().isConflict())
                .andReturn();
        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "USER_IN_GROUP");
    }


    @Test
    public void shouldReturnNotFoundGivenUserExistsAndGroupDoesntExist() throws Exception {
        //given
        final Group group = getGroup();
        groupRepository.save(group);

        //when
        final MvcResult result = mockMvc.perform(put("/user/1/add/group/1"))
                .andExpect(status().isNotFound())
                .andReturn();

        //then

        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "NO_USER");
    }

    @Test
    public void shouldDeleteUserFromGroupGivenUserIsInGroup() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final Group group = getGroup();
        groupRepository.save(group);
        userGroupService.save(1L, 1L);

        //when
        final MvcResult result = mockMvc.perform(delete("/user/1/delete/group/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<UserGroup> userGroups = userGroupRepository.findAll();
        assertThat(userGroups).isEmpty();
    }

    @Test
    public void shouldReturnConflictGivenUserIsNotInGroup() throws Exception {
        //given

        //when
        final MvcResult result = mockMvc.perform(delete("/user/1/delete/group/1"))
                .andExpect(status().isConflict())
                .andReturn();

        //then
        final String response = result.getResolvedException().getMessage();
        assertEquals(response, "USER_NOT_IN_GROUP");
    }

    @Test
    public void shouldGetListOfGroupsForUserGivenUserIsInGroup() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final Group group = getGroup();
        groupRepository.save(group);
        userGroupService.save(1L, 1L);

        //when
        final MvcResult result = mockMvc.perform(get("/user/1/groups"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[{\"id\":1,\"name\":\"group_name\",\"description\":\"group_description\"}]");

    }

    @Test
    public void shouldGetEmptyListOfGroupsForUserGivenUserIsNotInGroup() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);

        //when
        final MvcResult result = mockMvc.perform(get("/user/1/groups"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[]");

    }

    @Test
    public void shouldAddReminderForUser() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final ReminderRequest request = new ReminderRequest(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, PERIOD, LOCAL_TIME);
        //when
        final MvcResult result = mockMvc.perform(post("/user/1/add/reminder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).isNotEmpty();
        final Reminder reminder = reminders.get(0);
        assertEquals(reminder.getName(), REMINDER_NAME);
        assertEquals(reminder.getDescription(), REMINDER_DESCRIPTION);
        assertEquals(reminder.getRecurrence().getPeriod(), PERIOD);
        assertEquals(reminder.getDate(), LOCAL_DATE);
        assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"reminder_name\",\"description\":\"reminder_description\",\"date\":\"2023-01-29\",\"period\":228,\"time\":\"00:28:09.437523\",\"userId\":1,\"groupId\":null}");
    }

    @Test
    public void shouldDeleteReminderForUser() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, new Recurrence(null, PERIOD, LOCAL_TIME));
        userService.addReminder(1L, reminder);
        //when
        final MvcResult result = mockMvc.perform(delete("/user/1/delete/reminder/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        final List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).isEmpty();
    }

    @Test
    public void shouldGetRemindersListForUser() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        final Reminder reminder = new Reminder(null, REMINDER_NAME, REMINDER_DESCRIPTION, LOCAL_DATE, null, null, new Recurrence(null, PERIOD, LOCAL_TIME));
        userService.addReminder(1L, reminder);
        //when
        final MvcResult result = mockMvc.perform(get("/user/1/reminders"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[{\"id\":1,\"name\":\"reminder_name\",\"description\":\"reminder_description\",\"date\":\"2023-01-29\",\"period\":228,\"time\":\"00:28:09\",\"userId\":1,\"groupId\":null}]");
    }

    @Test
    public void shouldGetEmptyRemindersListForUserGivenUserHasNoReminders() throws Exception {
        //given
        final User user = createUser();
        userRepository.save(user);
        //when
        final MvcResult result = mockMvc.perform(get("/user/1/reminders"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertEquals(result.getResponse().getContentAsString(), "[]");
    }

    private User createUser() {
        return new User(1L, PASSWORD, NAME, SURNAME, EMAIL, Set.of(), List.of());
    }

    private Group getGroup() {
        return new Group(null, GROUP_NAME, GROUP_DESCRIPTION, Set.of(), List.of());
    }
}
